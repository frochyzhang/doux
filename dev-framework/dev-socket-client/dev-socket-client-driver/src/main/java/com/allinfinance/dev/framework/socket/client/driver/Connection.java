package com.allinfinance.dev.framework.socket.client.driver;

import com.allinfinance.dev.framework.extension.annotation.Extensible;

import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/14 9:27
 */
@Extensible
public interface Connection {
    String send(String msg);

    void connect(Properties properties);
}
