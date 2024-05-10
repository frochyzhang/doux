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
package cn.lezoo.doux.framework.conn.wrapper.unpooled;

import cn.lezoo.doux.framework.conn.driver.ServerMetadata;
import cn.lezoo.doux.framework.conn.driver.ServerMetadataFactory;
import cn.lezoo.doux.framework.conn.wrapper.constant.ConnectionConfig;
import cn.lezoo.doux.framework.extension.annotation.Extension;

import java.util.Properties;

import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.BUFFER_SIZE;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.DEFAULT_NETWORK_TIMEOUT;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.LENGTH_FIELD;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.SERVER_IP;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.SERVER_PORT;

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
        unpooledServerMetadata.setDefaultNetworkTimeout(Integer.parseInt(properties.getProperty(DEFAULT_NETWORK_TIMEOUT, "30")));

        Properties additional = new Properties();
        additional.setProperty(ConnectionConfig.CONNECTION_DRIVER, properties.getProperty(ConnectionConfig.CONNECTION_DRIVER, "default"));
        additional.setProperty(ConnectionConfig.PING_SERVICE, properties.getProperty(ConnectionConfig.PING_SERVICE, "default"));
        additional.setProperty(LENGTH_FIELD, properties.getProperty(LENGTH_FIELD, "2"));
        additional.setProperty(BUFFER_SIZE, properties.getProperty(BUFFER_SIZE, "65535"));
        additional.setProperty(DEFAULT_NETWORK_TIMEOUT, properties.getProperty(DEFAULT_NETWORK_TIMEOUT, "30"));
        unpooledServerMetadata.setAdditionalProperties(additional);

        metadata = unpooledServerMetadata;
    }

    @Override
    public ServerMetadata getMetadata() {
        return metadata;
    }
}
