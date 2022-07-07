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
package com.allinfinance.dev.framework.conn.wrapper.unpooled;

import com.allinfinance.dev.framework.conn.driver.ServerMetadata;
import com.allinfinance.dev.framework.conn.driver.ServerMetadataFactory;
import com.allinfinance.dev.framework.conn.wrapper.constant.ConnectionConfig;
import com.allinfinance.dev.framework.extension.annotation.Extension;

import java.util.Properties;

import static com.allinfinance.dev.framework.conn.wrapper.constant.ServerMetadataConfig.*;

/**
 * @author Clinton Begin
 */
@Extension("unPool")
public class UnpooledServerMetadataFactory implements ServerMetadataFactory {

    protected ServerMetadata metadata;

    public UnpooledServerMetadataFactory() {
    }

    @Override
    public void setProperties(Properties properties) {
        UnpooledServerMetadata unpooledServerMetadata = new UnpooledServerMetadata(properties.getProperty(SERVER_IP),
                Integer.parseInt(properties.getProperty(SERVER_PORT)));
        unpooledServerMetadata.setDefaultNetworkTimeout(Integer.parseInt(properties.getProperty(DEFAULT_NETWORK_TIMEOUT,"30")));

        Properties additional = new Properties();
        additional.setProperty(ConnectionConfig.CONNECTION_DRIVER, properties.getProperty(ConnectionConfig.CONNECTION_DRIVER, "netty"));
        additional.setProperty(ConnectionConfig.PING_SERVICE, properties.getProperty(ConnectionConfig.PING_SERVICE, "default"));
        unpooledServerMetadata.setAdditionalProperties(additional);

        metadata = unpooledServerMetadata;
    }

    @Override
    public ServerMetadata getMetadata() {
        return metadata;
    }
}
