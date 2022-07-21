package com.allinfinance.dev.framework.conn.wrapper.constant;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/4
 **/
public class ConnectionConfig {
    /**
     * 连接池驱动。如：netty
     */
    public static final String CONNECTION_DRIVER = "connectionDriver";
    /**
     * 连接池底层实现：双列表/阻塞队列
     */
    public static final String CONNECTION_POOL_TYPE = "connectionPoolType";
    /**
     * 连接检查服务别名，默认提供：defaultPingService
     */
    public static final String PING_SERVICE = "pingService";
}
