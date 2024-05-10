/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.lezoo.doux.framework.conn.wrapper.pooled;

import cn.lezoo.doux.framework.conn.wrapper.constant.ConnectionConfig;
import cn.lezoo.doux.framework.conn.wrapper.unpooled.UnpooledServerMetadataFactory;
import cn.lezoo.doux.framework.extension.annotation.Extension;

import java.util.Properties;

import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.BUFFER_SIZE;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.CONNECT_TIMEOUT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.DEFAULT_NETWORK_TIMEOUT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.LENGTH_FIELD;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.MAX_ACTIVE_CONNECTIONS;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.MAX_CHECKOUT_TIME;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.MAX_IDLE_CONNECTIONS;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.MAX_LOCAL_BAD_CONNECTION_TOLERANCE;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.PING_CONNECTIONS_NOT_USED;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.PING_ENABLED;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.PING_QUERY_CONTENT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.PING_VERIFY_CONTENT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.RETRY_TIME_TO_WAIT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.SERVER_IP;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.SERVER_PORT;

/**
 * @author Clinton Begin
 */
@Extension(value = "pool")
public class PooledServerMetadataFactory extends UnpooledServerMetadataFactory {

    public PooledServerMetadataFactory() {
        this.metadata = new PooledServerMetadata();
    }

    @Override
    public void setProperties(Properties properties) {
        PooledServerMetadata pooledServerMetadata = new PooledServerMetadata(properties.getProperty(SERVER_IP),
                Integer.parseInt(properties.getProperty(SERVER_PORT)));

        /* POOL */
        pooledServerMetadata.setDefaultNetworkTimeout(Integer.parseInt(properties.getProperty(DEFAULT_NETWORK_TIMEOUT, "30")));
        pooledServerMetadata.setMaxCheckoutTime(Integer.parseInt(properties.getProperty(MAX_CHECKOUT_TIME, "1200000")));
        pooledServerMetadata.setRetryTimeToWait(Integer.parseInt(properties.getProperty(RETRY_TIME_TO_WAIT, "10")));

        pooledServerMetadata.setMaxActiveConnections(Integer.parseInt(properties.getProperty(MAX_ACTIVE_CONNECTIONS, "10")));
        pooledServerMetadata.setMaxIdleConnections(Integer.parseInt(properties.getProperty(MAX_IDLE_CONNECTIONS, "5")));

        pooledServerMetadata.setPingEnabled(Boolean.parseBoolean(properties.getProperty(PING_ENABLED, "true")));
        pooledServerMetadata.setPingQueryContent(properties.getProperty(PING_QUERY_CONTENT, ""));
        pooledServerMetadata.setPingVerifyContent(properties.getProperty(PING_VERIFY_CONTENT, ""));
        pooledServerMetadata.setMaxLocalBadConnectionTolerance(Integer.parseInt(properties.getProperty(MAX_LOCAL_BAD_CONNECTION_TOLERANCE, "3")));
        pooledServerMetadata.setPingConnectionsNotUsed(Integer.parseInt(properties.getProperty(PING_CONNECTIONS_NOT_USED, "0")));

        /* additional properties */
        Properties additional = new Properties();
        additional.setProperty(ConnectionConfig.CONNECTION_DRIVER, properties.getProperty(ConnectionConfig.CONNECTION_DRIVER, "netty"));
        additional.setProperty(ConnectionConfig.PING_SERVICE, properties.getProperty(ConnectionConfig.PING_SERVICE, "default"));
        additional.setProperty(LENGTH_FIELD, properties.getProperty(LENGTH_FIELD, "2"));
        additional.setProperty(BUFFER_SIZE, properties.getProperty(BUFFER_SIZE, "65535"));
        additional.setProperty(DEFAULT_NETWORK_TIMEOUT, properties.getProperty(DEFAULT_NETWORK_TIMEOUT, "30"));
        additional.setProperty(CONNECT_TIMEOUT, properties.getProperty(CONNECT_TIMEOUT, "500"));
        pooledServerMetadata.getMetadata().setAdditionalProperties(additional);

        pooledServerMetadata.init();

        metadata = pooledServerMetadata;
    }
}
