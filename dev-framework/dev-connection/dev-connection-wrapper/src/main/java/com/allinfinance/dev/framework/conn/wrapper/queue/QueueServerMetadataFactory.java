package com.allinfinance.dev.framework.conn.wrapper.queue;

import com.allinfinance.dev.framework.conn.wrapper.constant.ConnectionConfig;
import com.allinfinance.dev.framework.conn.wrapper.unpooled.UnpooledServerMetadata;
import com.allinfinance.dev.framework.conn.wrapper.unpooled.UnpooledServerMetadataFactory;
import com.allinfinance.dev.framework.extension.annotation.Extension;

import java.util.Properties;

import static com.allinfinance.dev.framework.conn.wrapper.constant.ServerMetadataConfig.*;

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
        UnpooledServerMetadata unpooledServerMetadata = new UnpooledServerMetadata(properties.getProperty(SERVER_IP),
                Integer.parseInt(properties.getProperty(SERVER_PORT)));
        QueueServerMetadata queueServerMetadata = new QueueServerMetadata(unpooledServerMetadata);

        queueServerMetadata.setDefaultNetworkTimeout(Integer.parseInt(properties.getProperty(DEFAULT_NETWORK_TIMEOUT, "30")));
        queueServerMetadata.setMaxActiveConnections(Integer.parseInt(properties.getProperty(MAX_ACTIVE_CONNECTIONS, "10")));
        queueServerMetadata.setMaxCheckoutTime(Integer.parseInt(properties.getProperty(MAX_CHECKOUT_TIME, "5000")));
        queueServerMetadata.setPingEnabled(Boolean.parseBoolean(properties.getProperty(PING_ENABLED, "true")));
        queueServerMetadata.setPingQueryContent(properties.getProperty(PING_QUERY_CONTENT, ""));
        queueServerMetadata.setPingVerifyContent(properties.getProperty(PING_VERIFY_CONTENT, ""));

        /* additional properties */
        Properties additional = new Properties();
        additional.setProperty(ConnectionConfig.CONNECTION_DRIVER, properties.getProperty(ConnectionConfig.CONNECTION_DRIVER, "default"));
        additional.setProperty(ConnectionConfig.PING_SERVICE, properties.getProperty(ConnectionConfig.PING_SERVICE, "default"));
        additional.setProperty(LENGTH_FIELD, properties.getProperty(LENGTH_FIELD, "2"));
        additional.setProperty(BUFFER_SIZE, properties.getProperty(BUFFER_SIZE, "65535"));
        additional.setProperty(DEFAULT_NETWORK_TIMEOUT, properties.getProperty(DEFAULT_NETWORK_TIMEOUT, "30"));
        queueServerMetadata.getMetadata().setAdditionalProperties(additional);

        queueServerMetadata.init();
        metadata = queueServerMetadata;
    }
}
