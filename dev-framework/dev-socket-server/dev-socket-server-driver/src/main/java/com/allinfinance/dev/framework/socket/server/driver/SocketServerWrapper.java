package com.allinfinance.dev.framework.socket.server.driver;

import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/15 10:27
 */
public interface SocketServerWrapper {

    void start(List<Properties> propertyList);
}
