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
}
