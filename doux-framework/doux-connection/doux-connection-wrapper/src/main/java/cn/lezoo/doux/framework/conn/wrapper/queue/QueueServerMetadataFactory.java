package cn.lezoo.doux.framework.conn.wrapper.queue;

import cn.lezoo.doux.framework.conn.wrapper.constant.ConnectionConfig;
import cn.lezoo.doux.framework.conn.wrapper.unpooled.UnpooledServerMetadata;
import cn.lezoo.doux.framework.conn.wrapper.unpooled.UnpooledServerMetadataFactory;
import cn.lezoo.doux.framework.extension.annotation.Extension;

import java.util.Properties;

import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.BUFFER_SIZE;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.CONNECT_TIMEOUT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.DEFAULT_NETWORK_TIMEOUT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.LENGTH_FIELD;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.MAX_ACTIVE_CONNECTIONS;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.MAX_CHECKOUT_TIME;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.NAME;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.PING_ENABLED;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.PING_QUERY_CONTENT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.PING_VERIFY_CONTENT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.SERVER_IP;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.SERVER_PORT;

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
        unpooledServerMetadata.setDefaultNetworkTimeout(Integer.parseInt(properties.getProperty(DEFAULT_NETWORK_TIMEOUT, "500")));

        QueueServerMetadata queueServerMetadata = new QueueServerMetadata(unpooledServerMetadata);
        queueServerMetadata.setName(properties.getProperty(NAME, "queue-connection-pool"));
        queueServerMetadata.setMaxActiveConnections(Integer.parseInt(properties.getProperty(MAX_ACTIVE_CONNECTIONS, "10")));
        queueServerMetadata.setMaxCheckoutTime(Integer.parseInt(properties.getProperty(MAX_CHECKOUT_TIME, "1200000")));
        queueServerMetadata.setPingEnabled(Boolean.parseBoolean(properties.getProperty(PING_ENABLED, "true")));
        queueServerMetadata.setPingQueryContent(properties.getProperty(PING_QUERY_CONTENT, ""));
        queueServerMetadata.setPingVerifyContent(properties.getProperty(PING_VERIFY_CONTENT, ""));

        /* additional properties */
        Properties additional = new Properties();
        additional.setProperty(ConnectionConfig.CONNECTION_DRIVER, properties.getProperty(ConnectionConfig.CONNECTION_DRIVER, "default"));
        additional.setProperty(ConnectionConfig.PING_SERVICE, properties.getProperty(ConnectionConfig.PING_SERVICE, "default"));
        additional.setProperty(LENGTH_FIELD, properties.getProperty(LENGTH_FIELD, "2"));
        additional.setProperty(BUFFER_SIZE, properties.getProperty(BUFFER_SIZE, "65535"));
        additional.setProperty(DEFAULT_NETWORK_TIMEOUT, properties.getProperty(DEFAULT_NETWORK_TIMEOUT, "500"));
        additional.setProperty(CONNECT_TIMEOUT, properties.getProperty(CONNECT_TIMEOUT, "500"));
        queueServerMetadata.getMetadata().setAdditionalProperties(additional);

        queueServerMetadata.init();
        metadata = queueServerMetadata;
    }
}
