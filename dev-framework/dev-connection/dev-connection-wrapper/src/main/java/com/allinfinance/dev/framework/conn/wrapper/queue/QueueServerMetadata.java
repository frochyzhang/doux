package com.allinfinance.dev.framework.conn.wrapper.queue;

import com.allinfinance.dev.framework.conn.driver.Connection;
import com.allinfinance.dev.framework.conn.driver.PingService;
import com.allinfinance.dev.framework.conn.driver.ServerMetadata;
import com.allinfinance.dev.framework.conn.wrapper.constant.ConnectionConfig;
import com.allinfinance.dev.framework.conn.wrapper.constant.enums.ConnectionStatus;
import com.allinfinance.dev.framework.conn.wrapper.unpooled.UnpooledServerMetadata;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import org.apache.commons.lang3.StringUtils;
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
        state = new QueueState(this, this.maxActiveConnections);
    }

    /**
     * 初始化空闲连接池以及pingService
     */
    public void init() {
        for (int i = 0; i < maxActiveConnections; i++) {
            state.queue.add(new QueueConnection(this, metadata.getConnection()));
        }
        Properties properties = metadata.getAdditionalProperties();
        String pingServiceAlias = properties.getProperty(ConnectionConfig.PING_SERVICE);
        // pingService提供自定义扩展后，优先使用自定义扩展
        pingService = ExtensionLoaderFactory.getExtensionLoader(PingService.class)
                .getExtension(StringUtils.isNotBlank(pingServiceAlias) ? pingServiceAlias : "default");
    }

    /**
     * 新增连接
     */
    public void addConnection() {
        state.queue.add(new QueueConnection(this, metadata.getConnection()));
    }

    /**
     * 回收连接
     *
     * @param connection
     */
    public void pushConnection(QueueConnection connection) {
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
    public QueueConnection popConnection() {
        QueueConnection conn = state.queue.poll();
        if (conn != null) {
            if (System.currentTimeMillis() - conn.getLastUsedTimestamp() > this.getMaxCheckoutTime()) {
                if (this.pingConnection(conn)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("老头连接有效，返回该连接：{}", conn.hashCode());
                    }
                    pushConnection(conn);
                } else if (ConnectionStatus.TIMEOUT.equals(conn.getStatus())) {
                    // ping连接失败
                    if (logger.isDebugEnabled()) {
                        logger.debug("老头连接超时，等待重试：{}", conn.hashCode());
                    }
                    conn = null;
                } else if (ConnectionStatus.INACTIVE.equals(conn.getStatus())) {
                    // 连接重试失败，重新创建新连接
                    if (logger.isDebugEnabled()) {
                        logger.debug("连接失效，新建连接");
                    }
                    this.addConnection();
                    conn = null;
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("小鲜肉连接，返回该连接：{}", conn.hashCode());
                }
                pushConnection(conn);
            }
        }

        return conn;
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
     */
    public boolean pingConnection(QueueConnection conn) {
        boolean result;
        try {
            result = !pingEnabled || pingService.pingConnection(conn.getRealConnection(), pingQueryContent, pingVerifyContent, getDefaultNetworkTimeout());
            if (!result) {
                conn.setStatus(ConnectionStatus.TIMEOUT);
            }
        } catch (Throwable e) {
            logger.warn("Execution of ping query '" + pingQueryContent + "' failed: " + e.getMessage());
            try {
                conn.getRealConnection().close();
            } catch (Exception e2) {
                // ignore
            }
            result = false;
            conn.setStatus(ConnectionStatus.INACTIVE);
            if (logger.isDebugEnabled()) {
                logger.debug("Connection " + conn.getRealConnection().hashCode() + " is BAD: " + e.getMessage());
            }
        }
        return result;
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

    public int getMaxActiveConnections() {
        return maxActiveConnections;
    }

    public void setMaxActiveConnections(int maxActiveConnections) {
        this.maxActiveConnections = maxActiveConnections;
    }

    public Integer getDefaultNetworkTimeout() {
        return metadata.getDefaultNetworkTimeout();
    }

    public void setDefaultNetworkTimeout(Integer defaultNetworkTimeout) {
        metadata.setDefaultNetworkTimeout(defaultNetworkTimeout);
    }

    public int getMaxCheckoutTime() {
        return maxCheckoutTime;
    }

    public void setMaxCheckoutTime(int maxCheckoutTime) {
        this.maxCheckoutTime = maxCheckoutTime;
    }

    public String getPingQueryContent() {
        return pingQueryContent;
    }

    public void setPingQueryContent(String pingQueryContent) {
        this.pingQueryContent = pingQueryContent;
    }

    public String getPingVerifyContent() {
        return pingVerifyContent;
    }

    public void setPingVerifyContent(String pingVerifyContent) {
        this.pingVerifyContent = pingVerifyContent;
    }

    public boolean isPingEnabled() {
        return pingEnabled;
    }

    public void setPingEnabled(boolean pingEnabled) {
        this.pingEnabled = pingEnabled;
    }
}
