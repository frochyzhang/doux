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
package com.allinfinance.dev.framework.conn.wrapper.pooled;

import com.allinfinance.dev.framework.conn.wrapper.constant.ConnectionConfig;
import com.allinfinance.dev.framework.conn.wrapper.unpooled.UnpooledServerMetadataFactory;
import com.allinfinance.dev.framework.extension.annotation.Extension;

import java.util.Properties;

import static com.allinfinance.dev.framework.conn.wrapper.constant.ServerMetadataConfig.*;

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
        pooledServerMetadata.setMaxCheckoutTime(Integer.parseInt(properties.getProperty(MAX_CHECKOUT_TIME, "5000")));
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
        pooledServerMetadata.getMetadata().setAdditionalProperties(additional);

        pooledServerMetadata.init();

        metadata = pooledServerMetadata;
    }
}
