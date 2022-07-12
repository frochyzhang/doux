package com.allinfinance.dev.framework.conn.wrapper.constant;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/4
 **/
public class ServerMetadataConfig {
    /**
     * 服务端ip
     */
    public static final String SERVER_IP = "serverIp";
    /**
     * 服务端端口
     */
    public static final String SERVER_PORT = "serverPort";
    /**
     * 最大活跃连接数
     */
    public static final String MAX_ACTIVE_CONNECTIONS = "maxActiveConnections";
    /**
     * 最大空闲连接数
     */
    public static final String MAX_IDLE_CONNECTIONS = "maxIdleConnections";
    /**
     * 最大空闲连接检查时间，超过该时间后要检查连接活跃度，单位：毫秒
     */
    public static final String MAX_CHECKOUT_TIME = "maxCheckoutTime";
    /**
     * 最大的请求超时时间，超过该时间表示请求超时，单位：毫秒
     */
    public static final String DEFAULT_NETWORK_TIMEOUT = "defaultNetworkTimeout";
    /**
     * 连接重试等待时间，单位：毫秒
     */
    public static final String RETRY_TIME_TO_WAIT = "retryTimeToWait";
    /**
     * 本地能容忍的最大坏连接数
     */
    public static final String MAX_LOCAL_BAD_CONNECTION_TOLERANCE = "maxLocalBadConnectionTolerance";
    /**
     * ping请求内容
     */
    public static final String PING_QUERY_CONTENT = "pingQueryContent";
    /**
     * ping验证内容
     */
    public static final String PING_VERIFY_CONTENT = "pingVerifyContent";
    /**
     * ping开关
     */
    public static final String PING_ENABLED = "pingEnabled";
    /**
     * 最近一次使用该连接的时间差，单位：毫秒
     */
    public static final String PING_CONNECTIONS_NOT_USED = "pingConnectionsNotUsed";
    /**
     * 长度域长度，默认为: 2
     */
    public static final String LENGTH_FIELD = "lengthField";
    /**
     * 缓冲区大小，默认为: 65535
     */
    public static final String BUFFER_SIZE = "bufferSize";
}
