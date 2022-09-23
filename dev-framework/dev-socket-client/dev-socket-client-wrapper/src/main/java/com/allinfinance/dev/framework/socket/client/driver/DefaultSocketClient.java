package com.allinfinance.dev.framework.socket.client.driver;

import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022/09/15 10:27
 */
@Extension("default")
public class DefaultSocketClient implements SocketClient {
    private static final Logger logger = LoggerFactory.getLogger(DefaultSocketClient.class);
    /**
     * 请求信息发送
     *
     * @param properties 服务端连接信息
     * @param msg        请求内容
     * @return 响应内容
     */
    @Override
    public String send(Properties properties, String msg) {
        String connectionDriver = properties.getProperty("connectionDriver");
        ExtensionLoader<Connection> loader = ExtensionLoaderFactory.getExtensionLoader(Connection.class);
        Connection connection = loader.getExtension(connectionDriver);
        connection.connect(properties);
        try {
            String response = connection.send(msg);
            if (logger.isDebugEnabled()) {
                logger.debug("接收到响应：{}", response);
            }
            return response;
        } catch (Throwable e) {
            return null;
        }
    }
}
