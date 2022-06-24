package com.allinfinance.dev.connection.scaffold.api;

import io.netty.util.concurrent.Promise;

import java.util.concurrent.Future;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/6/24
 **/
public interface ClientConnection {
    /**
     * 创建长连接
     *
     * @param remoteIp   服务端ip
     * @param remotePort 服务端port
     * @param retryTimes 重试次数
     */
    void connect(String remoteIp, int remotePort, int retryTimes, int bufferSize);

    /**
     * 关闭连接
     */
    void close();

    /**
     * 发送请求
     * @param msg 请求内容
     * @return 响应内容
     */
    String send(String msg);
}


