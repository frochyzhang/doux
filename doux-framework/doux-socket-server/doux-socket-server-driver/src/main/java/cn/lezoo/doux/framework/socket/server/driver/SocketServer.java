package cn.lezoo.doux.framework.socket.server.driver;

import cn.lezoo.doux.framework.extension.annotation.Extensible;

import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/15 10:27
 */
@Extensible
public interface SocketServer {
    /**
     * 根据传入properties配置开启具体服务端口
     *
     * @param properties 服务端口配置参数
     */
    void start(Properties properties);

    /**
     * 关闭服务端口
     *
     * @param port 关闭端口号
     */
    void close(Integer port);
}
