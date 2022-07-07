package com.allinfinance.dev.connection.pool.scaffold.api;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/6/24
 **/
public interface MessagePorter {
    /**
     * 发送请求
     * @param msg 请求内容
     * @return 响应内容
     */
    String writeAndFlush(String msg);
}
