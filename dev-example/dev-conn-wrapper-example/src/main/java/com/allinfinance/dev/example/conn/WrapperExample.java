package com.allinfinance.dev.example.conn;

import cn.hutool.core.lang.UUID;
import com.allinfinance.dev.framework.conn.driver.Connection;
import com.allinfinance.dev.framework.conn.driver.ServerMetadata;
import com.allinfinance.dev.framework.conn.wrapper.pooled.PooledServerMetadataFactory;

import java.util.Properties;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/6/30
 **/
public class WrapperExample {
    public static void main(String[] args) {
        PooledServerMetadataFactory factory  = new PooledServerMetadataFactory();
        Properties properties = new Properties();
        properties.setProperty("serverIp", "localhost");
        properties.setProperty("serverPort", "9999");
        properties.setProperty("defaultNetworkTimeout", "200");
        properties.setProperty("maximumCheckoutTime", "1");
        properties.setProperty("timeToWait", "1");
        properties.setProperty("maximumActiveConnections", "12");
        properties.setProperty("mMaximumIdleConnections", "5");
        properties.setProperty("pingEnabled", "true");
        properties.setProperty("pingQuery", "fuck");
        properties.setProperty("maximumLocalBadConnectionTolerance", "1");
        properties.setProperty("pingConnectionsNotUsedFor", "200");

        factory.setProperties(properties);
        Connection connection = factory.getMetadata().getConnection();
        System.out.println(connection.send("alkdsjfalkdjalsdjalkd"));
    }
}
