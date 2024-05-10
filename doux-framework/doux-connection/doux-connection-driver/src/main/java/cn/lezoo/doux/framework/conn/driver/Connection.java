package cn.lezoo.doux.framework.conn.driver;

import cn.lezoo.doux.framework.extension.annotation.Extensible;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/6/29 14:27
 */
@Extensible(singleton = false)
public interface Connection {
    void setNetworkTimeout(ExecutorService executor, Integer timeout);

    void close();

    boolean isClosed();

    String send(String msg);

    void connect(Properties properties);
}
