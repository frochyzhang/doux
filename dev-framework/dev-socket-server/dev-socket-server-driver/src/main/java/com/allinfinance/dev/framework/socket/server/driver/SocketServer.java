package com.allinfinance.dev.framework.socket.server.driver;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/15 10:27
 */
public interface SocketServer {
    void start(Properties properties) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
    void close(Integer port);
}
