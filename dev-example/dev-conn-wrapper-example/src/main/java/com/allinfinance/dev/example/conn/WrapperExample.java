package com.allinfinance.dev.example.conn;

import com.allinfinance.dev.framework.conn.driver.ServerMetadataFactory;
import com.allinfinance.dev.framework.conn.wrapper.constant.ConnectionConfig;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;

import java.util.Properties;

import static com.allinfinance.dev.framework.conn.wrapper.constant.ServerMetadataConfig.*;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/6/30
 **/
public class WrapperExample {
    public static void main(String[] args) {
        /* 连接池的双列表实现 */
//        PooledServerMetadataFactory factory = new PooledServerMetadataFactory();
//        Properties properties = new Properties();
//        properties.setProperty("serverIp", "127.0.0.1");
//        properties.setProperty("serverPort", "9999");
//        properties.setProperty("defaultNetworkTimeout", "200");
//        properties.setProperty("maximumCheckoutTime", "1");
//        properties.setProperty("timeToWait", "1");
//        properties.setProperty("maximumActiveConnections", "12");
//        properties.setProperty("mMaximumIdleConnections", "5");
//        properties.setProperty("pingEnabled", "true");
//        properties.setProperty("pingQuery", "fuck");
//        properties.setProperty("maximumLocalBadConnectionTolerance", "1");
//        properties.setProperty("pingConnectionsNotUsedFor", "200");
//        properties.setProperty("connectionDriver", "netty");
//        factory.setProperties(properties);
//        System.out.println(factory.getMetadata().send("alkdsjfalkdjalsdjalkd"));

        /* 连接池的阻塞队列实现 */
        Properties queueProperties = new Properties();
        queueProperties.setProperty(SERVER_IP, "127.0.0.1");
        queueProperties.setProperty(SERVER_PORT, "9999");
        queueProperties.setProperty(MAX_CHECKOUT_TIME, "3000");
        queueProperties.setProperty(ConnectionConfig.CONNECTION_DRIVER, "netty");
        queueProperties.setProperty(ConnectionConfig.CONNECTION_POOL_TYPE, "queue");

        ExtensionLoader<ServerMetadataFactory> serverMetadataExtensionLoader = ExtensionLoaderFactory.getExtensionLoader(ServerMetadataFactory.class);
        ServerMetadataFactory queueMetadata = serverMetadataExtensionLoader.getExtension(queueProperties.getProperty(ConnectionConfig.CONNECTION_POOL_TYPE));

        queueMetadata.setProperties(queueProperties);

        System.out.println(queueMetadata.getMetadata().send("alsdjalksdjfas"));
    }
}
