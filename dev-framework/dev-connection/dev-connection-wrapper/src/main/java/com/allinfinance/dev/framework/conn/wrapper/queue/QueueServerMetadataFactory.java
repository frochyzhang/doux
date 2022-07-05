package com.allinfinance.dev.framework.conn.wrapper.queue;

import com.allinfinance.dev.framework.conn.wrapper.unpooled.UnpooledServerMetadata;
import com.allinfinance.dev.framework.conn.wrapper.unpooled.UnpooledServerMetadataFactory;
import com.allinfinance.dev.framework.extension.annotation.Extension;

import java.util.Properties;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/1
 **/
@Extension(value = "queue")
public class QueueServerMetadataFactory extends UnpooledServerMetadataFactory {

    public QueueServerMetadataFactory() {
        this.metadata = new QueueServerMetadata();
    }

    @Override
    public void setProperties(Properties properties) {
        UnpooledServerMetadata unpooledServerMetadata = new UnpooledServerMetadata(properties.getProperty("serverIp"),
                Integer.parseInt(properties.getProperty("serverPort")));
        QueueServerMetadata queueServerMetadata = new QueueServerMetadata(unpooledServerMetadata, properties);

//        queueServerMetadata.setDefaultNetworkTimeout(Integer.parseInt(properties.getProperty("defaultNetworkTimeout", "30")));
//        queueServerMetadata.setPoolMaximumCheckoutTime(Integer.parseInt(properties.getProperty("poolMaximumActiveConnections", "10")));
//        queueServerMetadata.setPoolTimeToWait(Integer.parseInt(properties.getProperty("requestTimeout", "10")));
//
//        queueServerMetadata.setPoolMaximumActiveConnections(Integer.parseInt(properties.getProperty("idleConnectionCheckoutTime", "5")));
//        queueServerMetadata.setPoolMaximumIdleConnections(Integer.parseInt(properties.getProperty("mMaximumIdleConnections", "5")));
//
//        queueServerMetadata.setPoolPingEnabled(Boolean.parseBoolean(properties.getProperty("poolPingEnabled", "true")));
//        queueServerMetadata.setPoolPingQuery(properties.getProperty("poolPingQuery", "hello"));
//        queueServerMetadata.setPoolMaximumLocalBadConnectionTolerance(Integer.parseInt(properties.getProperty("lengthField", "3")));

        /* additional properties */
        Properties additional = new Properties();
        additional.setProperty("connectionDriver", ((String) properties.get("connectionDriver")));
        queueServerMetadata.getMetadata().setAdditionalProperties(additional);

        queueServerMetadata.init();
        metadata = queueServerMetadata;
    }
}
