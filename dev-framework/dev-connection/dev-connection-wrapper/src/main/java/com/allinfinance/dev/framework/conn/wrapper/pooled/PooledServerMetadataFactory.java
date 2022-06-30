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

import com.allinfinance.dev.framework.conn.wrapper.unpooled.UnpooledServerMetadataFactory;

import java.util.Properties;

/**
 * @author Clinton Begin
 */
public class PooledServerMetadataFactory extends UnpooledServerMetadataFactory {

    public PooledServerMetadataFactory() {
        this.metadata = new PooledServerMetadata();
    }

    @Override
    public void setProperties(Properties properties) {
        PooledServerMetadata pooledServerMetadata = new PooledServerMetadata(properties.getProperty("serverIp"),
                Integer.parseInt(properties.getProperty("serverPort")));

        pooledServerMetadata.setDefaultNetworkTimeout(Integer.parseInt(properties.getProperty("defaultNetworkTimeout", "30")));
        pooledServerMetadata.setPoolMaximumCheckoutTime(Integer.parseInt(properties.getProperty("maximumCheckoutTime", "10")));
        pooledServerMetadata.setPoolTimeToWait(Integer.parseInt(properties.getProperty("timeToWait", "10")));

        pooledServerMetadata.setPoolMaximumActiveConnections(Integer.parseInt(properties.getProperty("maximumActiveConnections", "5")));
        pooledServerMetadata.setPoolMaximumIdleConnections(Integer.parseInt(properties.getProperty("mMaximumIdleConnections", "5")));

        pooledServerMetadata.setPoolPingEnabled(Boolean.parseBoolean(properties.getProperty("pingEnabled", "true")));
        pooledServerMetadata.setPoolPingQuery(properties.getProperty("pingQuery", "hello"));
        pooledServerMetadata.setPoolMaximumLocalBadConnectionTolerance(Integer.parseInt(properties.getProperty("maximumLocalBadConnectionTolerance", "3")));
        pooledServerMetadata.setPoolPingConnectionsNotUsedFor(Integer.parseInt(properties.getProperty("pingConnectionsNotUsedFor", "10")));
        metadata = pooledServerMetadata;
    }
}
