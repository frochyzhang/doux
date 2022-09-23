package com.allinfinance.dev.common.socket.server;

import com.allinfinance.dev.common.socket.server.Bean.SocketBean;

import java.util.List;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-16 14:53
 */
public interface ISocketServerService {
    /**
     * 根据传入的socketbeans开启多端口监听
     * @param socketBeans
     */
    void start(List<SocketBean> socketBeans);
}
