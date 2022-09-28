package com.allinfinance.dev.framework.socket.client.driver;

import com.allinfinance.dev.framework.extension.annotation.Extensible;

import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/14 9:27
 */
@Extensible
public interface Connection {
    /**
     * 客户端发送请求
     *
     * @param msg 请求信息
     * @return 服务端响应
     */
    String send(String msg);

    /**
     * 建立socket连接
     *
     * @param properties 客户端配置连接参数
     */
    void connect(Properties properties);
}
