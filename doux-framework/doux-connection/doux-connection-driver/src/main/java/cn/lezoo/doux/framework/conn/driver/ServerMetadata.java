package cn.lezoo.doux.framework.conn.driver;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/6/29 14:19
 */
public interface ServerMetadata {
    /**
     * 获取连接
     *
     * @return Connection
     */
    Connection getConnection();

    /**
     * 获取连接
     *
     * @param serverIp   服务端ip
     * @param serverPort 服务端端口
     * @return Connection
     */
    Connection getConnection(String serverIp, Integer serverPort);

    /**
     * 发送请求
     *
     * @param msg 请求报文
     * @return 响应报文
     */
    String send(String msg);
}
