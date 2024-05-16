package cn.lezoo.doux.framework.conn.wrapper.queue;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.driver.PingService;
import cn.lezoo.doux.framework.conn.driver.ServerMetadata;
import cn.lezoo.doux.framework.conn.wrapper.constant.ConnectionConfig;
import cn.lezoo.doux.framework.conn.wrapper.constant.enums.ConnectionStatus;
import cn.lezoo.doux.framework.conn.wrapper.unpooled.UnpooledServerMetadata;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qipeng
 * @date 2022/6/22 16:41
 * @description
 */
@Getter
@Setter
@Slf4j
public class QueueServerMetadata implements ServerMetadata {
    /**
     * 池名
     */
    private String name;
    /**
     * 池状态
     */
    private QueueState state;
    /**
     * 无池化的原始服务端元数据
     */
    private UnpooledServerMetadata metadata;
    /**
     * 池里面的最大活跃连接数
     */
    protected int maxActiveConnections = 10;
    /**
     * 空闲连接检查时间，单位：ms，默认值5秒
     */
    protected int maxCheckoutTime = 5000;
    /**
     * 连接检查请求内容
     */
    protected String pingQueryContent = "";
    /**
     * 连接检查校验内容
     */
    protected String pingVerifyContent = "";
    /**
     * 开启或禁用侦测查询
     */
    protected boolean pingEnabled = true;
    /**
     * 连接测试服务，用于校验连接是否正常
     */
    protected PingService pingService;

    public QueueServerMetadata() {
    }

    public QueueServerMetadata(UnpooledServerMetadata metadata) {
        this.metadata = metadata;
    }

    private ScheduledThreadPoolExecutor houseKeepingExecutorService;
    private ScheduledFuture<?> houseKeeperTask;
    private ThreadPoolExecutor addConnectionExecutor;
    private final ConnectionCreator connectionCreator = new ConnectionCreator();
    private final ConnectionCreator postFillConnectionCreator = new ConnectionCreator("After adding ");

    /**
     * 初始化空闲连接池以及pingService
     */
    public void init() {
        // 自动重连
        houseKeepingExecutorService = new ScheduledThreadPoolExecutor(1,
                new NamedThreadFactory(name + "-housekeeper-", true),
                new ThreadPoolExecutor.DiscardPolicy());
        houseKeepingExecutorService.setRemoveOnCancelPolicy(true);
        houseKeeperTask = houseKeepingExecutorService.scheduleWithFixedDelay(new HouseKeeper(), 100L, 1000L, TimeUnit.MILLISECONDS);
        addConnectionExecutor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(maxActiveConnections),
                new NamedThreadFactory(name + "-connectionCreator-", true),
                new ThreadPoolExecutor.DiscardPolicy());
        addConnectionExecutor.allowCoreThreadTimeOut(true);

        state = new QueueState(this, this.maxActiveConnections);
        // for (int i = 0; i < maxActiveConnections; i++) {
        //     this.addConnection();
        // }
        Properties properties = metadata.getAdditionalProperties();
        String pingServiceAlias = properties.getProperty(ConnectionConfig.PING_SERVICE);
        // pingService提供自定义扩展后，优先使用自定义扩展
        pingService = ExtensionLoaderFactory.getExtensionLoader(PingService.class)
                .getExtension(StringUtils.isNotBlank(pingServiceAlias) ? pingServiceAlias : "default");
    }

    /**
     * 新增连接
     */
    public QueueConnection addConnection() {
        Connection connection = metadata.getConnection();
        if (metadata.getDefaultNetworkTimeout() != null) {
            connection.setNetworkTimeout(Executors.newSingleThreadExecutor(), metadata.getDefaultNetworkTimeout());
        }

        QueueConnection queueConnection = new QueueConnection(this, connection);
        if (!isConnectionAlive(queueConnection)) {
            log.warn("连接补充失败");
            return null;
        }
        if (maxCheckoutTime > 0) {
            // variance up to 10% of the heartbeat time
            // final long variance = ThreadLocalRandom.current().nextLong(maxCheckoutTime / 10);
            // final long heartbeatTime = maxCheckoutTime - variance;
            // queueConnection.setKeepaliveTask(houseKeepingExecutorService.scheduleWithFixedDelay(new KeepaliveTask(queueConnection), heartbeatTime, heartbeatTime, TimeUnit.MILLISECONDS));
            queueConnection.setKeepaliveTask(houseKeepingExecutorService.scheduleWithFixedDelay(new KeepaliveTask(queueConnection), maxCheckoutTime, maxCheckoutTime, TimeUnit.MILLISECONDS));
        }

        pushConnection(queueConnection);
        return queueConnection;
    }

    /**
     * 回收连接
     *
     * @param connection
     */
    public void pushConnection(QueueConnection connection) {
        if (!ConnectionStatus.INACTIVE.equals(connection.getStatus())) {
            if (state.queue.size() < maxActiveConnections) {
                if (log.isDebugEnabled()) {
                    log.debug("回收连接：{}", connection.hashCode());
                }
                try {
                    state.queue.add(connection);
                } catch (Exception e) {
                    closeConnection(connection);
                    if (log.isDebugEnabled()) {
                        log.debug("当前连接数充足: {}，关闭连接：{}", state.queue.size(), connection.hashCode());
                    }
                }
            } else {
                // 否则，连接还比较充足，直接将connection关闭
                closeConnection(connection);
                if (log.isDebugEnabled()) {
                    log.debug("当前连接数充足: {}，关闭连接：{}", state.queue.size(), connection.hashCode());
                }
            }
        }
    }

    public boolean removeConnection(QueueConnection connection) {
        return state.queue.removeFirstOccurrence(connection);
    }

    /**
     * 获取连接
     *
     * @return
     */
    public QueueConnection popConnection() {
        return state.queue.poll();
    }

    /**
     * 关闭所有连接
     */
    public void forceCloseAll() {
        log.info("强制关闭所有连接！");
        while (!state.queue.isEmpty()) {
            QueueConnection connection = state.queue.remove();
            closeConnection(connection);
        }
        // if (houseKeeperTask != null) {
        //     houseKeeperTask.cancel(false);
        //     houseKeeperTask = null;
        // }
    }

    @Override
    protected void finalize() throws Throwable {
        forceCloseAll();
        super.finalize();
    }

    /**
     * 获取连接
     *
     * @return Connection
     */
    @Override
    public Connection getConnection() {
        QueueConnection connection = popConnection();
        if (connection == null) {
            return null;
        }
        return connection.getProxyConnection();
    }

    // 顶梁柱
    @Setter
    @Getter
    private final class HouseKeeper implements Runnable {
        private final Logger LOGGER = LoggerFactory.getLogger(HouseKeeper.class);

        @Override
        public void run() {
            int activeCount = getCount(ConnectionStatus.ACTIVE);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("触发houseKeeper，当前有效链接数:{}", activeCount);
            }
            final int connectionsToAdd = maxActiveConnections - activeCount;
            if (connectionsToAdd <= 0) {
                LOGGER.debug("{} - Fill pool skipped, pool is at sufficient level.", name);
            }

            for (int i = 0; i < connectionsToAdd; i++) {
                addConnectionExecutor.submit((i < connectionsToAdd - 1) ? connectionCreator : postFillConnectionCreator);
            }
        }
    }

    @Getter
    @Setter
    private final class KeepaliveTask implements Runnable {
        private final Logger LOGGER = LoggerFactory.getLogger(KeepaliveTask.class);

        private final QueueConnection connection;

        private KeepaliveTask(QueueConnection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("触发心跳,当前连接状态:{}", connection.getStatus());
            }
            if (removeConnection(connection) && isConnectionAlive(connection)) {
                pushConnection(connection);
            }
        }
    }

    /**
     * Creating and adding poolEntries (connections) to the pool.
     */
    private final class ConnectionCreator implements Runnable {
        private final Logger LOGGER = LoggerFactory.getLogger(ConnectionCreator.class);

        private String loggingPrefix;

        public ConnectionCreator() {
        }

        private ConnectionCreator(String loggingPrefix) {
            this.loggingPrefix = loggingPrefix;
        }

        @Override
        public void run() {
            long sleepBackoff = 250L;
            while (shouldCreateAnotherConnection()) {
                final QueueConnection connection = addConnection();
                if (connection != null) {
                    LOGGER.debug("{} - Added connection {}", name, connection.getRealConnection());
                    if (loggingPrefix != null) {
                        logPoolState(loggingPrefix);
                    }
                    return;
                }

                // failed to get connection from db, sleep and retry
                if (loggingPrefix != null) {
                    LOGGER.debug("{} - Connection add failed, sleeping with backoff: {}ms", name, sleepBackoff);
                }

                quietlySleep(sleepBackoff);
                sleepBackoff = Math.min(TimeUnit.SECONDS.toMillis(10), Math.min(maxCheckoutTime, (long) (sleepBackoff * 1.5)));
            }
        }

        /**
         * We only create connections if we need another idle connection or have threads still waiting
         * for a new connection.  Otherwise we bail out of the request to create.
         *
         * @return true if we should create a connection, false if the need has disappeared
         */
        private synchronized boolean shouldCreateAnotherConnection() {
            return getCount(ConnectionStatus.ACTIVE) < maxActiveConnections;
        }
    }

    private int getCount(ConnectionStatus status) {
        int count = 0;
        for (QueueConnection connection : state.queue) {
            if (connection.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    public Integer getDefaultNetworkTimeout() {
        return metadata.getDefaultNetworkTimeout();
    }

    public void setDefaultNetworkTimeout(Integer defaultNetworkTimeout) {
        metadata.setDefaultNetworkTimeout(defaultNetworkTimeout);
    }

    /**
     * Log the current pool state at debug level.
     *
     * @param prefix an optional prefix to prepend the log message
     */
    void logPoolState(String... prefix) {
        if (log.isDebugEnabled()) {
            log.debug("{} - {}stats (total={}, active={}, inactive={})",
                    name, (prefix.length > 0 ? prefix[0] : ""),
                    state.queue.size(), getCount(ConnectionStatus.ACTIVE), getCount(ConnectionStatus.INACTIVE));
        }
    }

    /**
     * Sleep and suppress InterruptedException (but re-signal it).
     *
     * @param millis the number of milliseconds to sleep
     */
    public static void quietlySleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // I said be quiet!
            Thread.currentThread().interrupt();
        }
    }

    private boolean isConnectionAlive(QueueConnection connection) {
        boolean result = false;
        try {
            long startTime = System.currentTimeMillis();
            String response = connection.getRealConnection().send(pingQueryContent);
            long endTime = System.currentTimeMillis();
            if (StringUtils.isNotBlank(pingVerifyContent)) {
                // 标准相应结果result不为空时进行ping相应的校验
                result = pingVerifyContent.equals(response)
                        && (endTime - startTime) <= getDefaultNetworkTimeout();
            } else {
                // 标准相应结果为空时，仅需要服务端相应不为空且在目标时间内即可
                result = ObjectUtils.allNotNull(response)
                        && (endTime - startTime) <= getDefaultNetworkTimeout();
            }
        } catch (Throwable e) {
            log.warn(
                    "Execution of ping query '" + pingQueryContent + "' failed: " + e.getMessage());
        }
        if (!result) {
            closeConnection(connection);

            log.warn("Connection " + connection.getRealConnection().hashCode() + " is BAD");
        }
        return result;
    }

    private void closeConnection(QueueConnection connection) {
        try {
            connection.getRealConnection().close();
        } catch (Exception e2) {
            log.error("关闭连接异常", e2);
        }
        connection.setStatus(ConnectionStatus.INACTIVE);
        ScheduledFuture<?> keepaliveTask = connection.getKeepaliveTask();
        if (keepaliveTask != null) {
            keepaliveTask.cancel(true);
            if (keepaliveTask.isCancelled()) {
                log.warn("keepalive任务已被取消");
            }
        }
    }
}
