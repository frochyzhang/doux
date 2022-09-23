package com.allinfinance.dev.socket.server.scaffold.api;

import com.allinfinance.dev.socket.server.scaffold.Bean.SocketBean;

import java.util.List;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-16 14:53
 */
public interface ISocketServerService {
    /**
     * 根据配置开启多端口服务端
     */
    void start();
}
