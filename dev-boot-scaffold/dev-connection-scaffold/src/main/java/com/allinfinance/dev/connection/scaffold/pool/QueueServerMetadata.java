package com.allinfinance.dev.connection.scaffold.pool;

import com.allinfinance.dev.connection.scaffold.api.ClientConnection;
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
 * @date 2022/6/22 16:41
 * @description
 */
public class QueueServerMetadata implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(QueueServerMetadata.class);

    /**
     * 池状态
     */
    private final QueueState state;
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
     * 连接请求超时时间，单位：ms
     */
    protected int requestTimeout = 20;
    /**
     * 空闲连接检查时间，单位：ms，默认值5分钟
     */
    protected int idleConnectionCheckoutTime = 5 * 60 * 1000;
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

    public QueueServerMetadata(ServerMetadata serverMetadata, int bufferSize, int lengthField) {
        this.serverMetadata = serverMetadata;
        this.poolMaximumActiveConnections = serverMetadata.getMaxActiveConnection();
        this.poolMaximumIdleConnections = serverMetadata.getMaxIdleConnection();
        this.requestTimeout = serverMetadata.getTimeout();
        this.poolPingEnabled = serverMetadata.getEnablePing();
        this.poolPingQuery = serverMetadata.getPingQueryMessage();
        this.poolPingVerify = serverMetadata.getPingVerifyMessage();
        this.bufferSize = bufferSize;
        this.lengthField = lengthField;
        this.idleConnectionCheckoutTime = serverMetadata.getCheckoutTime();
        state = new QueueState(this, this.poolMaximumActiveConnections);
    }

    /**
     * 初始化空闲连接池
     */
    public void init() {
        for (int i = 0; i < poolMaximumActiveConnections; i++) {
            state.queue.add(serverMetadata.fetchConnection(bufferSize, lengthField, requestTimeout));
        }
    }

    /**
     * 创建新连接直到达到最大连接数
     */
    protected void supplyConnections() {
        while (state.queue.size() < poolMaximumActiveConnections) {
            state.queue.add(serverMetadata.fetchConnection(bufferSize, lengthField, requestTimeout));
        }
    }

    /**
     * 新增连接
     */
    protected void addConnection() {
        state.queue.add(serverMetadata.fetchConnection(bufferSize, lengthField, requestTimeout));
    }

    /**
     * 回收连接
     *
     * @param connection
     */
    protected void pushConnection(AbstractClientConnection connection) {
        if (!ConnectionStatus.INACTIVE.equals(connection.getStatus())) {
            if (state.queue.size() < poolMaximumActiveConnections) {
                logger.info("回收连接：{}", connection.hashCode());
                state.queue.add(connection);
            } else {
                // 否则，连接还比较充足，直接将connection关闭
                connection.close();
                logger.info("当前连接数充足，关闭连接：{}", connection.hashCode());
            }
        } else {
            logger.info("该连接：{}属于无效连接，直接关闭", connection.hashCode());
            connection.close();
        }
    }

    /**
     * 获取连接
     *
     * @return
     */
    protected ClientConnection popConnection() {

        return state.queue.poll();
    }

    /**
     * 关闭所有连接
     */
    public void forceCloseAll() {
        logger.info("强制关闭所有连接！");
        while (state.queue.size() > 0) {
            ClientConnection connection = state.queue.remove();
            connection.close();
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
                result = pingWriteAndFlush(conn, poolPingQuery, poolPingVerify, requestTimeout);
            } catch (Exception e) {
                logger.error("连接：{}异常", conn.hashCode());
                conn.setStatus(ConnectionStatus.INACTIVE);
                conn.close();
                return false;
            }

            if (result) {
                logger.info("连接：{}正常！", conn.hashCode());
                conn.setStatus(ConnectionStatus.ACTIVE);
            } else {
                logger.error("发送连接测试报文：{}失败，服务端响应错误或超时", poolPingQuery);
                conn.setStatus(ConnectionStatus.TIMEOUT);
                logger.info("重试次数：{}", conn.retryTimes.decrementAndGet());
                if (conn.retryTimes.get() == 0) {
                    logger.info("重试次数超限，此连接无效：{}", conn.hashCode());
                    conn.setStatus(ConnectionStatus.INACTIVE);
                }
            }

        } else {
            logger.info("ping连接开关未打开，无需校验连接");
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
    public boolean pingWriteAndFlush(AbstractClientConnection connection, String msg, String result, int spendTime) throws SocketException {
        synchronized (connection) {
            long startTime = System.currentTimeMillis();
            String response = null;
            response = connection.send(msg);
            logger.info("ping报文响应：{}", response);
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
