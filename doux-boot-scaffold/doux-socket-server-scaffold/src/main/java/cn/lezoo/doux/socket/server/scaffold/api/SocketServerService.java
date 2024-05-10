package cn.lezoo.doux.socket.server.scaffold.api;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-16 14:53
 */
public interface SocketServerService {
    /**
     * 根据配置开启多端口服务端
     */
    void start();

    /**
     * 关闭服务端口号
     *
     * @param port 需要关闭的端口号
     */
    void close(Integer port);

}
