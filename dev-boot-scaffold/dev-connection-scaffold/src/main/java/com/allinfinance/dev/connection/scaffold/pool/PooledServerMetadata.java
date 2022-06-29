package com.allinfinance.dev.connection.scaffold.pool;

import com.allinfinance.dev.connection.scaffold.config.constant.ConnectionStatus;
import com.allinfinance.dev.connection.scaffold.metadata.ServerMetadata;
import com.allinfinance.dev.connection.scaffold.netty.connection.AbstractClientConnection;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import java.net.SocketException;

/**
 * @author qipeng
 * @date 2022/6/14 17:22
 * @description 连接池状态维护
 */
public class PooledServerMetadata implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(PooledServerMetadata.class);

    /**
     * 池状态
     */
    private final PoolState state = new PoolState(this);
    /**
     * 无池化的原始服务端元数据
     */
    private final ServerMetadata serverMetadata;
    /**
     * 池里面的最大活跃连接数
     */
    protected int poolMaximumActiveConnections = 10;
    /**
     * 池里面的最大空闲连接数
     */
    protected int poolMaximumIdleConnections = 5;
    /**
     * 在被强制返回之前,池中连接被检查的时间，单位：ms
     */
    protected int poolMaximumCheckoutTime = 20;
    /**
     * 连接检查请求内容
     */
    protected String poolPingQuery;
    /**
     * 连接检查校验内容
     */
    protected String poolPingVerify = "";
    /**
     * 开启或禁用侦测查询
     */
    protected boolean poolPingEnabled = true;
    /**
     * 接收缓冲区大小
     */
    protected int bufferSize;
    /**
     * 报文长度域
     */
    protected int lengthField;
    /**
     * 超时请求时间
     */
    protected int requestTimeout;

    public PooledServerMetadata(ServerMetadata serverMetadata, int bufferSize, int lengthField) {
        this.serverMetadata = serverMetadata;
        this.poolMaximumActiveConnections = serverMetadata.getMaxActiveConnection();
        this.poolMaximumIdleConnections = serverMetadata.getMaxIdleConnection();
        this.poolMaximumCheckoutTime = serverMetadata.getCheckoutTime();
        this.poolPingEnabled = serverMetadata.getEnablePing();
        this.poolPingQuery = serverMetadata.getPingQueryMessage();
        this.poolPingVerify = serverMetadata.getPingVerifyMessage();
        this.lengthField = lengthField;
        this.bufferSize = bufferSize;
        this.requestTimeout = serverMetadata.getTimeout();
    }

    /**
     * 初始化空闲连接池
     */
    public void init() {
        for (int i = 0; i < poolMaximumIdleConnections; i++) {
            state.idleConnections.add(serverMetadata.fetchConnection(bufferSize, lengthField, requestTimeout));
        }
    }

    /**
     * 回收连接
     *
     * @param connection
     */
    protected void pushConnection(AbstractClientConnection connection) {
        synchronized (state) {
            state.activeConnections.remove(connection);
            if (!ConnectionStatus.INACTIVE.equals(connection.getStatus())) {
                // 该连接状态不为失效，并且ping连接检查过后才进行下一步处理
                if (state.idleConnections.size() < poolMaximumIdleConnections) {
                    // 直接将连接加入到idle列表
                    state.idleConnections.add(connection);
                    if (logger.isDebugEnabled()) {
                        logger.debug("回收连接：{}加入空闲连接列表", connection.hashCode());
                    }

                    // 通知其他线程可以来抢连接了
                    state.notifyAll();
                } else {
                    // 否则，空闲链接还比较充足，直接将connection关闭
                    connection.close();
                    if (logger.isDebugEnabled()) {
                        logger.debug("连接：{}已关闭", connection.hashCode());
                    }
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("该连接：{}属于无效连接，直接关闭", connection.hashCode());
                }
                connection.close();
            }
        }
        logger.warn("当前线程：{},活跃连接数:{},空闲连接数:{}", Thread.currentThread(), state.activeConnections.size(), state.idleConnections.size());
    }

    /**
     * 优先获取空闲连接列表中的连接
     *
     * @return 没有获取到就返回null
     */
    public AbstractClientConnection popConnectionIfIdle() {
        synchronized (state) {
            if (!state.idleConnections.isEmpty()) {
                return popConnection();
            }
        }
        return null;
    }

    /**
     * 获取连接
     *
     * @return
     */
    protected AbstractClientConnection popConnection() {
        AbstractClientConnection conn = null;

        while (conn == null) {
            synchronized (state) {
                if (!state.idleConnections.isEmpty()) {
                    // 如果有空闲链接：返回第一个
                    conn = state.idleConnections.remove(0);
                    if (logger.isDebugEnabled()) {
                        logger.debug("从空闲连接列表中获取连接：{}", conn.hashCode());
                    }
                    // 从空闲连接列表取出来的长连接要先ping一下
                    pingConnection(conn);
                } else {
                    // 如果无空闲链接，则创建新的链接
                    if (state.activeConnections.size() < poolMaximumActiveConnections) {
                        // 活跃连接数没有超过最大活跃连接数，则创建新连接
                        conn = serverMetadata.fetchConnection(bufferSize, lengthField, requestTimeout);
                        // 新建的连接，先ping一下
                        pingConnection(conn);
                        if (logger.isDebugEnabled()) {
                            logger.debug("暂无空闲连接且活跃连接小于{}，创建新连接：{}", poolMaximumActiveConnections, conn.hashCode());
                        }
                    } else {
                        // 活跃连接数已满
                        // 取得活跃链接列表的第一个，也就是最老的一个连接
                        if (logger.isDebugEnabled()) {
                            logger.debug("取得活跃链接列表的第一个");
                        }
                        AbstractClientConnection oldestActiveConnection = state.activeConnections.get(0);
                        //先判断老的连接是否有效
                        if (ConnectionStatus.ACTIVE.equals(oldestActiveConnection.getStatus())) {
                            // 老连接有效时直接赋值
                            if (logger.isDebugEnabled()) {
                                logger.debug("老连接：{}有效，直接使用", oldestActiveConnection.hashCode());
                            }
                            conn = oldestActiveConnection;
                        } else if (ConnectionStatus.PENDING.equals(oldestActiveConnection.getStatus())) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("老连接：{}已使用，放回到空闲连接", oldestActiveConnection.hashCode());
                            }
                            pushConnection(oldestActiveConnection);
                        } else if (ConnectionStatus.TIMEOUT.equals(oldestActiveConnection.getStatus())) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("老连接：{}超时，先回收", oldestActiveConnection.hashCode());
                            }
                            pushConnection(oldestActiveConnection);
                        } else {
                            // 老连接无效时新建连接，并回收老来连接
                            conn = serverMetadata.fetchConnection(bufferSize, lengthField, requestTimeout);
                            if (logger.isDebugEnabled()) {
                                logger.debug("老连接无效，获取新连接：{}", conn.hashCode());
                            }
                            // 新建的连接，先ping一下
                            pingConnection(conn);
                        }
                    }
                }

                if (conn != null) {
                    // 获得到链接
                    if (ConnectionStatus.ACTIVE.equals(conn.getStatus())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("获取到连接：{}", conn.hashCode());
                        }
                        if (!state.activeConnections.contains(conn)) {
                            state.activeConnections.add(conn);
                        }
                    } else if (ConnectionStatus.INACTIVE.equals(conn.getStatus())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("无效的连接：{}，直接关闭", conn.hashCode());
                        }
                        conn.close();
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("超时的连接：{}，尝试获取新连接", conn.hashCode());
                        }
                        // 如果没拿到有效连接，将conn重新置为null，进行下一轮获取
                        conn = null;
                    }
                }
                state.notifyAll();
                logger.warn("当前线程：{}, pop的时候的活跃连接数:{},空闲连接数:{}", Thread.currentThread(), state.activeConnections.size(), state.idleConnections.size());
            }
        }

        return conn;
    }

    /**
     * 关闭所有连接
     */
    public void forceCloseAll() {
        synchronized (state) {
            // 关闭活跃链接
            for (int i = state.activeConnections.size(); i > 0; i--) {
                try {
                    AbstractClientConnection conn = state.activeConnections.remove(i - 1);
                    conn.close();
                } catch (Exception ignore) {

                }
            }
            // 关闭空闲链接
            for (int i = state.idleConnections.size(); i > 0; i--) {
                try {
                    AbstractClientConnection conn = state.idleConnections.remove(i - 1);
                    conn.close();
                } catch (Exception ignore) {

                }
            }
            logger.info("强制关闭所有连接！");
        }
    }


    /**
     * 连接检查
     *
     * @param conn
     * @return
     */
    protected boolean pingConnection(AbstractClientConnection conn) {
        boolean result;

        if (poolPingEnabled) {
            try {
                result = pingWriteAndFlush(conn, poolPingQuery, poolPingVerify, poolMaximumCheckoutTime);
            } catch (Exception e) {
                logger.error("连接：{}异常", conn.hashCode());
                conn.close();
                conn.setStatus(ConnectionStatus.INACTIVE);
                return false;
            }

            if (result) {
                if (logger.isDebugEnabled()) {
                    logger.debug("连接：{}正常！", conn.hashCode());
                }
                conn.setStatus(ConnectionStatus.ACTIVE);
            } else {
                logger.error("发送连接测试报文：{}失败，服务端相应错误或超时", poolPingQuery);
                conn.setStatus(ConnectionStatus.TIMEOUT);
                if (logger.isDebugEnabled()) {
                    logger.debug("重试次数：{}", conn.retryTimes.decrementAndGet());
                }
                if (conn.retryTimes.get() == 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("重试次数超限，此连接无效：{}", conn.hashCode());
                    }
                    conn.setStatus(ConnectionStatus.INACTIVE);
                }
            }

        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("ping连接开关未打开，无需校验连接");
            }
            conn.setStatus(ConnectionStatus.ACTIVE);
            result = true;
        }

        return result;
    }

    /**
     * 发送测试ping请求
     *
     * @param connection netty连接
     * @param msg        测试ping报文内容
     * @param result     测试ping相应内容
     * @param spendTime  测试ping的耗时（单位：ms）
     * @return
     */
    public boolean pingWriteAndFlush(AbstractClientConnection connection, String msg, String result, int spendTime) {
        synchronized (connection) {
            long startTime = System.currentTimeMillis();
            String response = null;
            try {
                response = connection.send(msg);
            } catch (SocketException e) {
                connection.setStatus(ConnectionStatus.INACTIVE);
                logger.error("请求发送异常");
                return false;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("ping报文响应：{}", response);
            }
            long endTime = System.currentTimeMillis();
            if (StringUtils.isNotBlank(result)) {
                //标准相应结果result不为空时进行ping相应的校验
                return result.equals(response) && (endTime - startTime) <= spendTime;
            } else {
                //标准相应结果为空时，仅需要服务端相应不为空且在目标时间内即可
                return ObjectUtils.allNotNull(response) && (endTime - startTime) <= spendTime;
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        forceCloseAll();
    }
}
