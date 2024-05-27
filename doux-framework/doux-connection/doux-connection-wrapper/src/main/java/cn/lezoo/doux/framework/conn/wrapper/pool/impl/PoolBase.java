/*
 * Copyright (C) 2013, 2014 Brett Wooldridge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.lezoo.doux.framework.conn.wrapper.pool.impl;

import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.wrapper.constant.ConnectionConfig;
import cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig;
import cn.lezoo.doux.framework.conn.wrapper.pool.HikariConfig;
import cn.lezoo.doux.framework.conn.wrapper.pool.exception.PoolException;
import cn.lezoo.doux.framework.conn.wrapper.pool.metrics.IMetricsTracker;
import cn.lezoo.doux.framework.conn.wrapper.unpooled.UnpooledServerMetadata;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.LoggerFactory;

import static cn.lezoo.doux.framework.conn.wrapper.pool.util.ClockSource.currentTime;
import static cn.lezoo.doux.framework.conn.wrapper.pool.util.ClockSource.elapsedMillis;
import static cn.lezoo.doux.framework.conn.wrapper.pool.util.ClockSource.elapsedNanos;

@Slf4j
abstract class PoolBase {
    private UnpooledServerMetadata unpooledServerMetadata;

    public final HikariConfig config;
    IMetricsTrackerDelegate metricsTracker;

    protected final String poolName;

    final AtomicReference<Exception> lastConnectionFailure;

    long connectionTimeout;
    long validationTimeout;

    /**
     * 连接检查请求内容
     */
    protected String pingQueryContent = "00";
    /**
     * 连接检查校验内容
     */
    protected String pingVerifyContent = "";

    private static final int UNINITIALIZED = -1;
    private static final int TRUE = 1;
    private static final int FALSE = 0;

    PoolBase(final HikariConfig config) {
        this.config = config;

        this.poolName = config.getPoolName();
        this.connectionTimeout = config.getConnectionTimeout();
        this.validationTimeout = config.getValidationTimeout();
        this.lastConnectionFailure = new AtomicReference<>();

        initiateMetaData();
    }

    private void initiateMetaData() {
        this.unpooledServerMetadata = new UnpooledServerMetadata("localhost", 9999);
        Properties properties = new Properties();
        properties.setProperty(ConnectionConfig.CONNECTION_DRIVER, "hsp");
        properties.setProperty(ServerMetadataConfig.LENGTH_FIELD, "2");
        properties.setProperty(ServerMetadataConfig.BUFFER_SIZE, "8192");
        properties.setProperty(ServerMetadataConfig.DEFAULT_NETWORK_TIMEOUT, "500");
        properties.setProperty(ServerMetadataConfig.CONNECT_TIMEOUT, "500");
        this.unpooledServerMetadata.setAdditionalProperties(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return poolName;
    }

    abstract void recycle(final PoolEntry poolEntry);

    // ***********************************************************************
    //                           JDBC methods
    // ***********************************************************************

    void quietlyCloseConnection(final Connection connection, final String closureReason) {
        if (connection != null) {
            try {
                log.debug("{} - Closing connection {}: {}", poolName, connection, closureReason);
                connection.close();
            } catch (Exception e) {
                log.debug("{} - Closing connection {} failed", poolName, connection, e);
            }
        }
    }

    boolean isConnectionDead(final Connection connection) {
        try {
            final int validationSeconds = (int) Math.max(1000L, validationTimeout) / 1000;

            return connection.isValid(validationSeconds, pingQueryContent);
        } catch (Exception e) {
            lastConnectionFailure.set(e);
            log.warn("{} - Failed to validate connection {} ({}). Possibly consider using a shorter maxLifetime value.",
                poolName, connection, e.getMessage());
            return true;
        }
    }

    Exception getLastConnectionFailure() {
        return lastConnectionFailure.get();
    }

    // ***********************************************************************
    //                         PoolEntry methods
    // ***********************************************************************

    PoolEntry newPoolEntry() throws Exception {
        return new PoolEntry(newConnection(), this);
    }

    // ***********************************************************************
    //                       JMX methods
    // ***********************************************************************

    /**
     * Register MBeans for HikariConfig and HikariPool.
     *
     * @param hikariPool a HikariPool instance
     */
    void handleMBeans(final HikariPool hikariPool, final boolean register) {
        if (!config.isRegisterMbeans()) {
            return;
        }

        try {
            final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

            ObjectName beanConfigName, beanPoolName;
            if ("true".equals(System.getProperty("hikaricp.jmx.register2.0"))) {
                beanConfigName =
                    new ObjectName("cn.lezoo.doux.framework.conn.wrapper.pool:type=PoolConfig,name=" + poolName);
                beanPoolName = new ObjectName("cn.lezoo.doux.framework.conn.wrapper.pool:type=Pool,name=" + poolName);
            } else {
                beanConfigName =
                    new ObjectName("cn.lezoo.doux.framework.conn.wrapper.pool:type=PoolConfig (" + poolName + ")");
                beanPoolName = new ObjectName("cn.lezoo.doux.framework.conn.wrapper.pool:type=Pool (" + poolName + ")");
            }
            if (register) {
                if (!mBeanServer.isRegistered(beanConfigName)) {
                    mBeanServer.registerMBean(config, beanConfigName);
                    mBeanServer.registerMBean(hikariPool, beanPoolName);
                } else {
                    log.error("{} - JMX name ({}) is already registered.", poolName, poolName);
                }
            } else if (mBeanServer.isRegistered(beanConfigName)) {
                mBeanServer.unregisterMBean(beanConfigName);
                mBeanServer.unregisterMBean(beanPoolName);
            }
        } catch (Exception e) {
            log.warn("{} - Failed to {} management beans.", poolName, (register ? "register" : "unregister"), e);
        }
    }

    // ***********************************************************************
    //                          Private methods
    // ***********************************************************************

    /**
     * Obtain connection from data source.
     *
     * @return a connection
     */
    private Connection newConnection() throws Exception {
        final long start = currentTime();

        Connection connection = null;
        try {
            connection = unpooledServerMetadata.getConnection();
            if (connection == null) {
                throw new PoolException("DataSource returned null unexpectedly");
            }

            setupConnection(connection);
            lastConnectionFailure.set(null);
            return connection;
        } catch (Exception e) {
            if (connection != null) {
                quietlyCloseConnection(connection, "(Failed to create/setup connection)");
            } else if (getLastConnectionFailure() == null) {
                log.debug("{} - Failed to create/setup connection: {}", poolName, e.getMessage());
            }

            lastConnectionFailure.set(e);
            throw e;
        } finally {
            // tracker will be null during failFast check
            if (metricsTracker != null) {
                metricsTracker.recordConnectionCreated(elapsedMillis(start));
            }
        }
    }

    /**
     * Setup a connection initial state.
     *
     * @param connection a Connection
     * @throws ConnectionSetupException thrown if any exception is encountered
     */
    private void setupConnection(final Connection connection) throws ConnectionSetupException {
        try {
            final int validationSeconds = (int) Math.max(1000L, validationTimeout) / 1000;
            if (!connection.isValid(validationSeconds, pingQueryContent)) {
                log.warn(
                    "{} - Connection {} failed alive test with query '{}'; consider configuring the connection test query.",
                    poolName, connection, pingQueryContent);
                throw new PoolException("Connection is not alive, possibly failed the validation query test.");
            }
        } catch (PoolException e) {
            throw new ConnectionSetupException(e);
        }
    }

    // ***********************************************************************
    //                      Private Static Classes
    // ***********************************************************************


    static class ConnectionSetupException extends Exception {
        private static final long serialVersionUID = 929872118275916521L;

        ConnectionSetupException(Throwable t) {
            super(t);
        }
    }


    /**
     * Special executor used only to work around a MySQL issue that has not been addressed.
     * MySQL issue: <a href="http://bugs.mysql.com/bug.php?id=75615">...</a>
     */
    private static class SynchronousExecutor implements Executor {
        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("NullableProblems")
        public void execute(Runnable command) {
            try {
                command.run();
            } catch (Exception t) {
                LoggerFactory.getLogger(PoolBase.class).debug("Failed to execute: {}", command, t);
            }
        }
    }


    interface IMetricsTrackerDelegate extends AutoCloseable {
        default void recordConnectionUsage(PoolEntry poolEntry) {
        }

        default void recordConnectionCreated(long connectionCreatedMillis) {
        }

        default void recordBorrowTimeoutStats(long startTime) {
        }

        default void recordBorrowStats(final PoolEntry poolEntry, final long startTime) {
        }

        default void recordConnectionTimeout() {
        }

        @Override
        default void close() {
        }
    }


    /**
     * A class that delegates to a MetricsTracker implementation.  The use of a delegate
     * allows us to use the NopMetricsTrackerDelegate when metrics are disabled, which in
     * turn allows the JIT to completely optimize away to callsites to record metrics.
     */
    static class MetricsTrackerDelegate implements IMetricsTrackerDelegate {
        final IMetricsTracker tracker;

        MetricsTrackerDelegate(IMetricsTracker tracker) {
            this.tracker = tracker;
        }

        @Override
        public void recordConnectionUsage(final PoolEntry poolEntry) {
            tracker.recordConnectionUsageMillis(poolEntry.getMillisSinceBorrowed());
        }

        @Override
        public void recordConnectionCreated(long connectionCreatedMillis) {
            tracker.recordConnectionCreatedMillis(connectionCreatedMillis);
        }

        @Override
        public void recordBorrowTimeoutStats(long startTime) {
            tracker.recordConnectionAcquiredNanos(elapsedNanos(startTime));
        }

        @Override
        public void recordBorrowStats(final PoolEntry poolEntry, final long startTime) {
            final long now = currentTime();
            poolEntry.lastBorrowed = now;
            tracker.recordConnectionAcquiredNanos(elapsedNanos(startTime, now));
        }

        @Override
        public void recordConnectionTimeout() {
            tracker.recordConnectionTimeout();
        }

        @Override
        public void close() {
            tracker.close();
        }
    }


    /**
     * A no-op implementation of the IMetricsTrackerDelegate that is used when metrics capture is
     * disabled.
     */
    static final class NopMetricsTrackerDelegate implements IMetricsTrackerDelegate {
    }
}
