package com.allinfinance.dev.framework.socket.server.driver;

import com.allinfinance.dev.framework.extension.annotation.Extensible;

import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/15 10:27
 */
@Extensible
public interface SocketServerWrapper {
    /**
     * 根据配置开启服务端口
     *
     * @param propertyList 服务端配置集合
     */
    void start(List<Properties> propertyList);

    /**
     * 关闭服务端口
     *
     * @param port 关闭端口号
     */
    void close(Integer port);

    /**
     * 关闭此服务所有端口
     */
    void closeAll();
}
