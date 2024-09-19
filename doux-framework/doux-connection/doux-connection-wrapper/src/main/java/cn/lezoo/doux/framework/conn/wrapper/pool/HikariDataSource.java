/*
 * Copyright (C) 2013 Brett Wooldridge
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

package cn.lezoo.doux.framework.conn.wrapper.pool;

import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.driver.ServerMetadata;
import cn.lezoo.doux.framework.conn.wrapper.pool.exception.PoolException;
import cn.lezoo.doux.framework.conn.wrapper.pool.impl.HikariPool;
import cn.lezoo.doux.framework.conn.wrapper.pool.impl.HikariPool.PoolInitializationException;
import cn.lezoo.doux.framework.conn.wrapper.pool.metrics.MetricsTrackerFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;

import static cn.lezoo.doux.framework.conn.wrapper.pool.impl.HikariPool.POOL_NORMAL;

/**
 * The HikariCP pooled DataSource.
 *
 * @author Brett Wooldridge
 */
@Slf4j
public class HikariDataSource extends HikariConfig implements ServerMetadata, Closeable {

    private final AtomicBoolean isShutdown = new AtomicBoolean();

    private final HikariPool fastPathPool;
    private volatile HikariPool pool;

    /**
     * Default constructor.  Setters are used to configure the pool.  Using
     * this constructor vs. {@link #HikariDataSource(HikariConfig)} will
     * result in {@link #getConnection()} performance that is slightly lower
     * due to lazy initialization checks.
     * <p>
     * The first call to {@link #getConnection()} starts the pool.  Once the pool
     * is started, the configuration is "sealed" and no further configuration
     * changes are possible -- except via {@link HikariConfigMXBean} methods.
     */
    public HikariDataSource() {
        fastPathPool = null;
    }

    /**
     * Construct a HikariDataSource with the specified configuration.  The
     * {@link HikariConfig} is copied and the pool is started by invoking this
     * constructor.
     * <p>
     * The {@link HikariConfig} can be modified without affecting the HikariDataSource
     * and used to initialize another HikariDataSource instance.
     *
     * @param configuration a HikariConfig instance
     */
    public HikariDataSource(HikariConfig configuration) {
        configuration.validate();
        configuration.copyStateTo(this);

        log.info("{} - Starting...", configuration.getPoolName());
        pool = fastPathPool = new HikariPool(this);
        log.info("{} - Start completed.", configuration.getPoolName());

        this.seal();
    }

    // ***********************************************************************
    //                          DataSource methods
    // ***********************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() {
        if (isClosed()) {
            throw new PoolException("HikariDataSource " + this + " has been closed.");
        }

        if (fastPathPool != null) {
            return fastPathPool.getConnection();
        }

        // See http://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
        HikariPool result = pool;
        if (result == null) {
            synchronized (this) {
                result = pool;
                if (result == null) {
                    validate();
                    log.info("{} - Starting...", getPoolName());
                    try {
                        pool = result = new HikariPool(this);
                        this.seal();
                    } catch (PoolInitializationException pie) {
                        log.error("{} - Exception during pool initialization.", getPoolName(), pie);
                        throw pie;
                    }
                    log.info("{} - Start completed.", getPoolName());
                }
            }
        }

        return result.getConnection();
    }

    // ***********************************************************************
    //                        HikariConfigMXBean methods
    // ***********************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMetricRegistry(Object metricRegistry) {
        boolean isAlreadySet = getMetricRegistry() != null;
        super.setMetricRegistry(metricRegistry);

        HikariPool p = pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricRegistry can only be set one time");
            } else {
                p.setMetricRegistry(super.getMetricRegistry());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        boolean isAlreadySet = getMetricsTrackerFactory() != null;
        super.setMetricsTrackerFactory(metricsTrackerFactory);

        HikariPool p = pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricsTrackerFactory can only be set one time");
            } else {
                p.setMetricsTrackerFactory(super.getMetricsTrackerFactory());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHealthCheckRegistry(Object healthCheckRegistry) {
        boolean isAlreadySet = getHealthCheckRegistry() != null;
        super.setHealthCheckRegistry(healthCheckRegistry);

        HikariPool p = pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("HealthCheckRegistry can only be set one time");
            } else {
                p.setHealthCheckRegistry(super.getHealthCheckRegistry());
            }
        }
    }

    // ***********************************************************************
    //                        HikariCP-specific methods
    // ***********************************************************************

    /**
     * Returns {@code true} if the pool as been started and is not suspended or shutdown.
     *
     * @return {@code true} if the pool as been started and is not suspended or shutdown.
     */
    public boolean isRunning() {
        return pool != null && pool.poolState == POOL_NORMAL;
    }

    /**
     * Get the {@code HikariPoolMXBean} for this HikariDataSource instance.  If this method is called on
     * a {@code HikariDataSource} that has been constructed without a {@code HikariConfig} instance,
     * and before an initial call to {@code #getConnection()}, the return value will be {@code null}.
     *
     * @return the {@code HikariPoolMXBean} instance, or {@code null}.
     */
    public HikariPoolMXBean getHikariPoolMXBean() {
        return pool;
    }

    /**
     * Get the {@code HikariConfigMXBean} for this HikariDataSource instance.
     *
     * @return the {@code HikariConfigMXBean} instance.
     */
    public HikariConfigMXBean getHikariConfigMXBean() {
        return this;
    }

    /**
     * Evict a connection from the pool.  If the connection has already been closed (returned to the pool)
     * this may result in a "soft" eviction; the connection will be evicted sometime in the future if it is
     * currently in use.  If the connection has not been closed, the eviction is immediate.
     *
     * @param connection the connection to evict from the pool
     */
    public void evictConnection(Connection connection) {
        HikariPool p;
        if (!isClosed() && (p = pool) != null && connection.getClass().getName().startsWith("cn.lezoo.doux.framework.conn.wrapper.pool")) {
            p.evictConnection(connection);
        }
    }

    /**
     * Shutdown the DataSource and its associated pool.
     */
    @Override
    public void close() {
        if (isShutdown.getAndSet(true)) {
            return;
        }

        HikariPool p = pool;
        if (p != null) {
            try {
                log.info("{} - Shutdown initiated...", getPoolName());
                p.shutdown();
                log.info("{} - Shutdown completed.", getPoolName());
            } catch (InterruptedException e) {
                log.warn("{} - Interrupted during closing", getPoolName(), e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Determine whether the HikariDataSource has been closed.
     *
     * @return true if the HikariDataSource has been closed, false otherwise
     */
    public boolean isClosed() {
        return isShutdown.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "HikariDataSource (" + pool + ")";
    }
}
