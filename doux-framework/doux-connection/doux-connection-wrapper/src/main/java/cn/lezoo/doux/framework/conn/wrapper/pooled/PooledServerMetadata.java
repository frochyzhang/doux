/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.lezoo.doux.framework.conn.wrapper.pooled;

import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.driver.PingService;
import cn.lezoo.doux.framework.conn.driver.ServerMetadata;
import cn.lezoo.doux.framework.conn.wrapper.constant.ConnectionConfig;
import cn.lezoo.doux.framework.conn.wrapper.unpooled.UnpooledServerMetadata;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Properties;

/**
 * This is a simple, synchronous, thread-safe database connection pool.
 *
 * @author Clinton Begin
 */
public class PooledServerMetadata implements ServerMetadata {

    private static final Logger logger = LoggerFactory.getLogger(PooledServerMetadata.class);

    private final PoolState state = new PoolState(this);

    private final UnpooledServerMetadata metadata;

    // OPTIONAL CONFIGURATION FIELDS
    protected PingService pingService;
    protected int maxActiveConnections = 10;
    protected int maxIdleConnections = 5;
    protected int maxCheckoutTime = 100;
    protected int retryTimeToWait = 10;
    protected int maxLocalBadConnectionTolerance = 3;
    protected String pingQueryContent = "";
    protected String pingVerifyContent = "";
    protected boolean pingEnabled = true;
    protected int pingConnectionsNotUsed;
    private int expectedConnectionTypeCode;

    public PooledServerMetadata() {
        metadata = new UnpooledServerMetadata();
    }

    public PooledServerMetadata(UnpooledServerMetadata metadata) {
        this.metadata = metadata;
    }

    public PooledServerMetadata(String serverIp, Integer serverPort) {
        metadata = new UnpooledServerMetadata(serverIp, serverPort);
        expectedConnectionTypeCode = assembleConnectionTypeCode(metadata.getServerIp(), metadata.getServerPort());
    }

    @Override
    public Connection getConnection() {
        return popConnection(metadata.getServerIp(), metadata.getServerPort()).getProxyConnection();
    }

    @Override
    public Connection getConnection(String serverIp, Integer serverPort) {
        return popConnection(serverIp, serverPort).getProxyConnection();
    }

    @Override
    public String send(String msg) {
        return getConnection().send(msg);
    }

    public void setServerIp(String serverIp) {
        metadata.setServerIp(serverIp);
        forceCloseAll();
    }

    public void setServerPort(Integer serverPort) {
        metadata.setServerPort(serverPort);
        forceCloseAll();
    }


    public void setDefaultNetworkTimeout(Integer milliseconds) {
        metadata.setDefaultNetworkTimeout(milliseconds);
        forceCloseAll();
    }

    /**
     * The maximum number of active connections.
     *
     * @param maxActiveConnections The maximum number of active connections
     */
    public void setMaxActiveConnections(int maxActiveConnections) {
        this.maxActiveConnections = maxActiveConnections;
        forceCloseAll();
    }

    /**
     * The maximum number of idle connections.
     *
     * @param maxIdleConnections The maximum number of idle connections
     */
    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
        forceCloseAll();
    }

    /**
     * The maximum number of tolerance for bad connection happens in one thread
     * which are applying for new {@link PooledConnection}.
     *
     * @param maxLocalBadConnectionTolerance max tolerance for bad connection happens in one thread
     * @since 3.4.5
     */
    public void setMaxLocalBadConnectionTolerance(
            int maxLocalBadConnectionTolerance) {
        this.maxLocalBadConnectionTolerance = maxLocalBadConnectionTolerance;
    }

    /**
     * The maximum time a connection can be used before it *may* be
     * given away again.
     *
     * @param maxCheckoutTime The maximum time
     */
    public void setMaxCheckoutTime(int maxCheckoutTime) {
        this.maxCheckoutTime = maxCheckoutTime;
        forceCloseAll();
    }

    /**
     * The time to wait before retrying to get a connection.
     *
     * @param retryTimeToWait The time to wait
     */
    public void setRetryTimeToWait(int retryTimeToWait) {
        this.retryTimeToWait = retryTimeToWait;
        forceCloseAll();
    }

    /**
     * The query to be used to check a connection.
     *
     * @param pingQueryContent The query
     */
    public void setPingQueryContent(String pingQueryContent) {
        this.pingQueryContent = pingQueryContent;
        forceCloseAll();
    }

    /**
     * Determines if the ping query should be used.
     *
     * @param pingEnabled True if we need to check a connection before using it
     */
    public void setPingEnabled(boolean pingEnabled) {
        this.pingEnabled = pingEnabled;
        forceCloseAll();
    }

    /**
     * If a connection has not been used in this many milliseconds, ping the
     * database to make sure the connection is still good.
     *
     * @param milliseconds the number of milliseconds of inactivity that will trigger a ping
     */
    public void setPingConnectionsNotUsed(int milliseconds) {
        this.pingConnectionsNotUsed = milliseconds;
        forceCloseAll();
    }

    public String getServerIp() {
        return metadata.getServerIp();
    }

    public Integer getServerPort() {
        return metadata.getServerPort();
    }

    /**
     * Gets the default network timeout.
     *
     * @return the default network timeout
     * @since 3.5.2
     */
    public Integer getDefaultNetworkTimeout() {
        return metadata.getDefaultNetworkTimeout();
    }

    public int getMaxActiveConnections() {
        return maxActiveConnections;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public int getMaxLocalBadConnectionTolerance() {
        return maxLocalBadConnectionTolerance;
    }

    public int getMaxCheckoutTime() {
        return maxCheckoutTime;
    }

    public int getRetryTimeToWait() {
        return retryTimeToWait;
    }

    public String getPingQueryContent() {
        return pingQueryContent;
    }

    public boolean isPingEnabled() {
        return pingEnabled;
    }

    public int getPingConnectionsNotUsed() {
        return pingConnectionsNotUsed;
    }

    public UnpooledServerMetadata getMetadata() {
        return metadata;
    }

    public String getPingVerifyContent() {
        return pingVerifyContent;
    }

    public void setPingVerifyContent(String pingVerifyContent) {
        this.pingVerifyContent = pingVerifyContent;
    }

    /**
     * 初始化pingService
     */
    public void init() {
        Properties properties = metadata.getAdditionalProperties();
        String pingServiceAlias = properties.getProperty(ConnectionConfig.PING_SERVICE);
        // pingService提供自定义扩展后，优先使用自定义扩展
        pingService = ExtensionLoaderFactory.getExtensionLoader(PingService.class)
                .getExtension(StringUtils.isNotBlank(pingServiceAlias) ? pingServiceAlias : "default");
    }

    /**
     * Closes all active and idle connections in the pool.
     */
    public void forceCloseAll() {
        synchronized (state) {
            expectedConnectionTypeCode = assembleConnectionTypeCode(metadata.getServerIp(), metadata.getServerPort());
            for (int i = state.activeConnections.size(); i > 0; i--) {
                try {
                    PooledConnection conn = state.activeConnections.remove(i - 1);
                    conn.invalidate();

                    Connection realConn = conn.getRealConnection();
                    realConn.close();
                } catch (Exception e) {
                    // ignore
                }
            }
            for (int i = state.idleConnections.size(); i > 0; i--) {
                try {
                    PooledConnection conn = state.idleConnections.remove(i - 1);
                    conn.invalidate();

                    Connection realConn = conn.getRealConnection();
                    realConn.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("PooledServerMetadata forcefully closed/removed all connections.");
        }
    }

    public PoolState getPoolState() {
        return state;
    }

    private int assembleConnectionTypeCode(String serverIp, Integer serverPort) {
        return ("" + serverIp + serverPort).hashCode();
    }

    protected void pushConnection(PooledConnection conn) {

        synchronized (state) {
            state.activeConnections.remove(conn);
            if (conn.isValid()) {
                if (state.idleConnections.size() < maxIdleConnections && conn.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    state.accumulatedCheckoutTime += conn.getCheckoutTime();
                    PooledConnection newConn = new PooledConnection(conn.getRealConnection(), this);
                    state.idleConnections.add(newConn);
                    newConn.setCreatedTimestamp(conn.getCreatedTimestamp());
                    newConn.setLastUsedTimestamp(conn.getLastUsedTimestamp());
                    conn.invalidate();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Returned connection " + newConn.getRealHashCode() + " to pool.");
                    }
                    state.notifyAll();
                } else {
                    state.accumulatedCheckoutTime += conn.getCheckoutTime();
                    conn.getRealConnection().close();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Closed connection " + conn.getRealHashCode() + ".");
                    }
                    conn.invalidate();
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("A bad connection (" + conn.getRealHashCode() + ") attempted to return to the pool, discarding connection.");
                }
                state.badConnectionCount++;
            }
        }
    }

    private PooledConnection popConnection(String serverIp, Integer serverPort) {
        boolean countedWait = false;
        PooledConnection conn = null;
        long t = System.currentTimeMillis();
        int localBadConnectionCount = 0;

        while (conn == null) {
            synchronized (state) {
                if (!state.idleConnections.isEmpty()) {
                    // Pool has available connection
                    conn = state.idleConnections.remove(0);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Checked out connection " + conn.getRealHashCode() + " from pool.");
                    }
                } else {
                    // Pool does not have available connection
                    if (state.activeConnections.size() < maxActiveConnections) {
                        // Can create new connection
                        conn = new PooledConnection(metadata.getConnection(), this);
                        if (logger.isDebugEnabled()) {
                            logger.debug("Created connection " + conn.getRealHashCode() + ".");
                        }
                    } else {
                        // Cannot create new connection
                        PooledConnection oldestActiveConnection = state.activeConnections.get(0);
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        if (longestCheckoutTime > maxCheckoutTime) {
                            // Can claim overdue connection
                            state.claimedOverdueConnectionCount++;
                            state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            state.accumulatedCheckoutTime += longestCheckoutTime;
                            state.activeConnections.remove(oldestActiveConnection);
                            conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
                            conn.setCreatedTimestamp(oldestActiveConnection.getCreatedTimestamp());
                            conn.setLastUsedTimestamp(oldestActiveConnection.getLastUsedTimestamp());
                            oldestActiveConnection.invalidate();
                            if (logger.isDebugEnabled()) {
                                logger.debug("Claimed overdue connection " + conn.getRealHashCode() + ".");
                            }
                        }
//                        else {
//                            // Must wait
//                            try {
//                                if (!countedWait) {
//                                    state.hadToWaitCount++;
//                                    countedWait = true;
//                                }
//                                if (logger.isDebugEnabled()) {
//                                    logger.debug("Waiting as long as " + poolTimeToWait + " milliseconds for connection.");
//                                }
//                                long wt = System.currentTimeMillis();
//                                state.wait(poolTimeToWait);
//                                state.accumulatedWaitTime += System.currentTimeMillis() - wt;
//                            } catch (InterruptedException e) {
//                                // set interrupt flag
//                                Thread.currentThread().interrupt();
//                                break;
//                            }
//                        }
                    }
                }
                if (conn != null) {
                    // ping to server and check the connection is valid or not
                    if (conn.isValid()) {
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(serverIp, serverPort));
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        state.activeConnections.add(conn);
                        state.requestCount++;
                        state.accumulatedRequestTime += System.currentTimeMillis() - t;
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("A bad connection (" + conn.getRealHashCode() + ") was returned from the pool, getting another connection.");
                        }
                        state.badConnectionCount++;
                        localBadConnectionCount++;
                        conn = null;
                        if (localBadConnectionCount > (maxIdleConnections + maxLocalBadConnectionTolerance)) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("PooledServerMetadata: Could not get a good connection to the database.");
                            }
                            throw new RuntimeException("PooledServerMetadata: Could not get a good connection to the database.");
                        }
                    }
                }
            }

        }

        if (conn == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("PooledServerMetadata: Unknown severe error condition.  The connection pool returned a null connection.");
            }
            throw new RuntimeException("PooledServerMetadata: Unknown severe error condition.  The connection pool returned a null connection.");
        }

        return conn;
    }

    /**
     * Method to check to see if a connection is still usable
     *
     * @param conn - the connection to check
     * @return True if the connection is still usable
     */
    protected boolean pingConnection(PooledConnection conn) {
        boolean result;

        try {
            result = !conn.getRealConnection().isClosed();
        } catch (RuntimeException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
            }
            result = false;
        }

        if (result && pingEnabled && pingConnectionsNotUsed >= 0
                && conn.getTimeElapsedSinceLastUse() > pingConnectionsNotUsed) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Testing connection " + conn.getRealHashCode() + " ...");
                }
                Connection realConn = conn.getRealConnection();
                result = pingService.pingConnection(realConn, pingQueryContent, pingVerifyContent, getDefaultNetworkTimeout());
                if (logger.isDebugEnabled()) {
                    logger.debug("Connection " + conn.getRealHashCode() + " is GOOD!");
                }
            } catch (Exception e) {
                logger.warn("Execution of ping query '" + pingQueryContent + "' failed: " + e.getMessage());
                try {
                    conn.getRealConnection().close();
                } catch (Exception e2) {
                    // ignore
                }
                result = false;
                if (logger.isDebugEnabled()) {
                    logger.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * Unwraps a pooled connection to get to the 'real' connection
     *
     * @param conn - the pooled connection to unwrap
     * @return The 'real' connection
     */
    public static Connection unwrapConnection(Connection conn) {
        if (Proxy.isProxyClass(conn.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(conn);
            if (handler instanceof PooledConnection) {
                return ((PooledConnection) handler).getRealConnection();
            }
        }
        return conn;
    }

    @Override
    protected void finalize() throws Throwable {
        forceCloseAll();
        super.finalize();
    }
}
