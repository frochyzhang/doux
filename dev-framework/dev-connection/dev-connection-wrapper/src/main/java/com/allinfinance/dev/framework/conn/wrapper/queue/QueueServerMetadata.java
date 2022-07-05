package com.allinfinance.dev.framework.conn.wrapper.queue;

import com.allinfinance.dev.framework.conn.driver.Connection;
import com.allinfinance.dev.framework.conn.driver.PingService;
import com.allinfinance.dev.framework.conn.driver.ServerMetadata;
import com.allinfinance.dev.framework.conn.wrapper.constant.ServerMetadataConfig;
import com.allinfinance.dev.framework.conn.wrapper.constant.enums.ConnectionStatus;
import com.allinfinance.dev.framework.conn.wrapper.unpooled.UnpooledServerMetadata;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author qipeng
 * @date 2022/6/22 16:41
 * @description
 */
public class QueueServerMetadata implements ServerMetadata {
    private static final Logger logger = LoggerFactory.getLogger(QueueServerMetadata.class);

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
     * 连接请求超时时间，单位：ms
     */
    protected int maxRequestTimeout = 20;
    /**
     * 空闲连接检查时间，单位：ms，默认值5分钟
     */
    protected int maxCheckoutTime = 5 * 60 * 1000;
    /**
     * 连接检查请求内容
     */
    protected String pingQueryContent;
    /**
     * 连接检查校验内容
     */
    protected String pingVerifyContent = "";
    /**
     * 开启或禁用侦测查询
     */
    protected boolean pingEnabled = true;

    public QueueServerMetadata() {
    }

    public QueueServerMetadata(UnpooledServerMetadata metadata, Properties properties) {
        this.metadata = metadata;

        this.maxCheckoutTime = Integer.parseInt(properties.getProperty(ServerMetadataConfig.MAX_CHECKOUT_TIME));
        state = new QueueState(this, this.maxActiveConnections);
    }

    /**
     * 初始化空闲连接池
     */
    public void init() {
        for (int i = 0; i < maxActiveConnections; i++) {
            state.queue.add(new QueueConnection(this, metadata.getConnection()));
        }
    }

    /**
     * 新增连接
     */
    protected void addConnection() {
        state.queue.add(new QueueConnection(this, metadata.getConnection()));
    }

    /**
     * 回收连接
     *
     * @param connection
     */
    protected void pushConnection(QueueConnection connection) {
        if (!ConnectionStatus.INACTIVE.equals(connection.getStatus())) {
            if (state.queue.size() < maxActiveConnections) {
                if (logger.isDebugEnabled()) {
                    logger.debug("回收连接：{}", connection.hashCode());
                }
                state.queue.add(connection);
            } else {
                // 否则，连接还比较充足，直接将connection关闭
                connection.setStatus(ConnectionStatus.INACTIVE);
                if (logger.isDebugEnabled()) {
                    logger.debug("当前连接数充足，关闭连接：{}", connection.hashCode());
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("该连接：{}属于无效连接，直接关闭", connection.hashCode());
            }
            connection.getRealConnection().close();
        }
    }

    /**
     * 获取连接
     *
     * @return
     */
    protected QueueConnection popConnection() {
        return state.queue.poll();
    }

    /**
     * 关闭所有连接
     */
    public void forceCloseAll() {
        logger.info("强制关闭所有连接！");
        while (state.queue.size() > 0) {
            QueueConnection connection = state.queue.remove();
            connection.getRealConnection().close();
        }
    }


    /**
     * 连接检查
     *
     * @param conn
     * @return
     */
    protected boolean pingConnection(QueueConnection conn) {
        boolean result;
        PingService pingService = ExtensionLoaderFactory.getExtensionLoader(PingService.class).getExtension("pingService");
        if (pingService != null) {
            return pingService.pingConnection(conn.getProxyConnection(), pingQueryContent, pingVerifyContent, maxRequestTimeout);
        }
        return true;
//        if (pingEnabled) {
//            try {
//                result = (conn, pingQuery, pingVerify, requestTimeout);
//            } catch (Exception e) {
//                logger.error("连接：{}异常", conn.hashCode());
//                conn.setStatus(ConnectionStatus.INACTIVE);
//                conn.close();
//                return false;
//            }
//
//            if (result) {
//                if (logger.isDebugEnabled()) {
//                    logger.debug("连接：{}正常！", conn.hashCode());
//                }
//                conn.setStatus(ConnectionStatus.ACTIVE);
//            } else {
//                logger.error("发送连接测试报文：{}失败，服务端响应错误或超时", pingQuery);
//                conn.setStatus(ConnectionStatus.TIMEOUT);
//                if (conn.retryTimes.get() == 0) {
//                    if (logger.isDebugEnabled()) {
//                        logger.debug("重试次数超限，此连接无效：{}", conn.hashCode());
//                    }
//                    conn.setStatus(ConnectionStatus.INACTIVE);
//                }
//            }
//
//        } else {
//            if (logger.isDebugEnabled()) {
//                logger.debug("ping连接开关未打开，无需校验连接");
//            }
//            conn.setStatus(ConnectionStatus.ACTIVE);
//            result = true;
//        }

//        return result;
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
        return popConnection().getProxyConnection();
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
        return getConnection().send(msg);
    }

    public UnpooledServerMetadata getMetadata() {
        return metadata;
    }
}
