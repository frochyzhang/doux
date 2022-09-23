package com.allinfinance.dev.framework.socket.server.driver;

import com.allinfinance.dev.framework.extension.annotation.Extensible;
import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/15 10:27
 */
@Extensible
public interface SocketServer {
    void start(Properties properties);

    void close(Integer port);
}
