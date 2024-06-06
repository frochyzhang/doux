package cn.lezoo.doux.framework.conn.wrapper.queue;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.driver.ServerMetadata;
import cn.lezoo.doux.framework.conn.wrapper.constant.enums.ConnectionStatus;
import cn.lezoo.doux.framework.conn.wrapper.unpooled.UnpooledServerMetadata;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
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
     * 正在被使用的连接
     */
    private Set<QueueConnection> usedConnections;
    /**
     * 池里面的最大活跃连接数
     */
    protected int maxActiveConnections = 10;
    /**
     * 空闲连接检查时间，单位：ms，默认值5秒
     */
    protected int maxCheckoutTime;
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
     * 连接补充间隔，单位：ms，默认10秒
     */
    protected int houseKeepInterval;

    public QueueServerMetadata() {
    }

    public QueueServerMetadata(UnpooledServerMetadata metadata) {
        this.metadata = metadata;
    }

    private ScheduledThreadPoolExecutor houseKeepingExecutorService;
    private ScheduledThreadPoolExecutor keepaliveExecutorService;
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
        houseKeeperTask = houseKeepingExecutorService.scheduleWithFixedDelay(new HouseKeeper(), 100L, houseKeepInterval, TimeUnit.MILLISECONDS);
        // 心跳探活
        keepaliveExecutorService = new ScheduledThreadPoolExecutor(10,
                new NamedThreadFactory(name + "-keepalive-", true),
                new ThreadPoolExecutor.DiscardPolicy());
        keepaliveExecutorService.setRemoveOnCancelPolicy(true);
        // 建立连接
        addConnectionExecutor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(maxActiveConnections),
                new NamedThreadFactory(name + "-connectionCreator-", true),
                new ThreadPoolExecutor.DiscardPolicy());
        addConnectionExecutor.allowCoreThreadTimeOut(true);
        // 正在使用的连接
        usedConnections = new ConcurrentHashSet<>();
        // 连接池状态
        state = new QueueState(this, this.maxActiveConnections);
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
            log.error("新增连接不可用，请检查配置是否正确");
            return null;
        }
        if (pingEnabled && maxCheckoutTime > 0) {
            // variance up to 10% of the heartbeat time
            // final long variance = ThreadLocalRandom.current().nextLong(maxCheckoutTime / 10);
            // final long heartbeatTime = maxCheckoutTime - variance;
            // queueConnection.setKeepaliveTask(houseKeepingExecutorService.scheduleWithFixedDelay(new KeepaliveTask(queueConnection), heartbeatTime, heartbeatTime, TimeUnit.MILLISECONDS));
            queueConnection.setKeepaliveTask(keepaliveExecutorService.scheduleWithFixedDelay(new KeepaliveTask(queueConnection), maxCheckoutTime, maxCheckoutTime, TimeUnit.MILLISECONDS));
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
                    usedConnections.remove(connection);
                    connection.setLastUsedTimestamp(System.currentTimeMillis());
                    state.queue.add(connection);
                } catch (Exception e) {
                    connection.closeConnection();
                    log.warn("当前连接数充足: {}，关闭连接：{}", state.queue.size(), connection.hashCode());
                }
            } else {
                // 否则，连接还比较充足，直接将connection关闭
                connection.closeConnection();
                log.warn("当前连接数充足: {}，关闭连接：{}", state.queue.size(), connection.hashCode());
            }
        }
    }

    public boolean removeTemporarily(QueueConnection connection) {
        boolean result = state.queue.removeFirstOccurrence(connection);
        if (result) {
            usedConnections.add(connection);
        }
        return result;
    }

    /**
     * 关闭所有连接
     */
    public void forceCloseAll() {
        log.info("强制关闭所有连接！");
        while (!state.queue.isEmpty()) {
            QueueConnection connection = state.queue.remove();
            connection.closeConnection();
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
        QueueConnection connection = state.queue.poll();
        if (connection == null) {
            return null;
        }
        usedConnections.add(connection);
        return connection.getProxyConnection();
    }

    /**
     * 获取连接
     *
     * @param serverIp   服务端ip
     * @param serverPort 服务端端口
     * @return Connection
     */
    @Override
    public Connection getConnection(String serverIp, Integer serverPort) {
        // FIXME: 2022/7/1 暂未实现
        return null;
    }

    /**
     * 发送请求
     *
     * @param msg 请求报文
     * @return 响应报文
     */
    @Override
    public String send(String msg) {
        Connection connection = getConnection();
        String response = connection.send(msg);
        pushConnection((QueueConnection) connection);
        return response;
    }

    // 顶梁柱
    @Setter
    @Getter
    private final class HouseKeeper implements Runnable {
        private final Logger logger = LoggerFactory.getLogger(HouseKeeper.class);

        @Override
        public void run() {
            int activeCount = getActiveCount();
            logger.info("触发houseKeeper，当前有效连接数: {}", activeCount);
            final int connectionsToAdd = maxActiveConnections - activeCount;
            if (connectionsToAdd == 0) {
                logger.info("{} - Fill pool skipped, pool is at sufficient level.", name);
            } else if (connectionsToAdd < 0) {
                logger.warn("{} - Active active connections count is over max active connections.", name);
            }

            for (int i = 0; i < connectionsToAdd; i++) {
                addConnectionExecutor.submit((i < connectionsToAdd - 1) ? connectionCreator : postFillConnectionCreator);
            }
        }
    }

    @Getter
    @Setter
    private final class KeepaliveTask implements Runnable {
        private final Logger logger = LoggerFactory.getLogger(KeepaliveTask.class);

        private final QueueConnection connection;

        private KeepaliveTask(QueueConnection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            // 连接未使用时间需要大于maxCheckoutTime
            if (connection.getTimeElapsedSinceLastUse() >= maxCheckoutTime && removeTemporarily(connection)) {
                logger.info("触发心跳，当前连接状态: {}, id: {}", connection.getStatus(), connection.getHashCode());
                if (isConnectionAlive(connection)) {
                    pushConnection(connection);
                }
            }
        }
    }

    /**
     * Creating and adding poolEntries (connections) to the pool.
     */
    private final class ConnectionCreator implements Runnable {
        private final Logger logger = LoggerFactory.getLogger(ConnectionCreator.class);

        private String loggingPrefix;

        private ConnectionCreator() {
        }

        private ConnectionCreator(String loggingPrefix) {
            this.loggingPrefix = loggingPrefix;
        }

        @Override
        public void run() {
            final QueueConnection connection = addConnection();
            if (connection != null) {
                logger.debug("{} - Added connection {}", name, connection.getRealConnection());
                if (loggingPrefix != null) {
                    logPoolState(loggingPrefix);
                }
                return;
            }

            // failed to get connection from db, sleep and retry
            logger.debug("{} - Connection add failed", name);
        }
    }

    private int getActiveCount() {
        return state.queue.size() + usedConnections.size();
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
            log.debug("{} - {}stats (total={}, notInUse={}, used={})",
                    name, (prefix.length > 0 ? prefix[0] : ""),
                    getActiveCount(), state.queue.size(), usedConnections.size());
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
            connection.closeConnection();

            log.warn("Connection " + connection.getRealConnection().hashCode() + " is BAD");
        }
        return result;
    }
}
