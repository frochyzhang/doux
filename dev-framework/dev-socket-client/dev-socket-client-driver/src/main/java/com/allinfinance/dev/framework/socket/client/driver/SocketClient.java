package com.allinfinance.dev.framework.socket.client.driver;

import com.allinfinance.dev.framework.extension.annotation.Extensible;

import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/14 16:27
 */
@Extensible
public interface SocketClient {
    /**
     * 请求信息发送
     *
     * @param properties 服务端连接信息
     * @param msg        请求内容
     * @return 响应内容
     */
    String send(Properties properties, String msg);
}
