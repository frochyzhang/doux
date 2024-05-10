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

import cn.lezoo.doux.framework.conn.driver.Connection;
import cn.lezoo.doux.framework.conn.driver.ServerMetadata;
import cn.lezoo.doux.framework.conn.wrapper.constant.ConnectionConfig;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoader;
import cn.lezoo.doux.framework.extension.loader.ExtensionLoaderFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;
import java.util.concurrent.Executors;

import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.SERVER_IP;
import static cn.lezoo.doux.framework.conn.wrapper.constant.ServerMetadataConfig.SERVER_PORT;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
@Getter
@Setter
public class UnpooledServerMetadata implements ServerMetadata {

    private Integer defaultNetworkTimeout;

    private String serverIp;

    private Integer serverPort;

    private Properties additionalProperties;

    public UnpooledServerMetadata() {
    }

    public UnpooledServerMetadata(String serverIp, Integer serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    @Override
    public Connection getConnection() {
        return doGetConnection(serverIp, serverPort);
    }

    @Override
    public Connection getConnection(String serverIp, Integer serverPort) {
        return doGetConnection(serverIp, serverPort);
    }

    @Deprecated
    @Override
    public String send(String msg) {
        return getConnection().send(msg);
    }

    public void setDefaultNetworkTimeout(Integer defaultNetworkTimeout) {
        this.defaultNetworkTimeout = defaultNetworkTimeout;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public void setAdditionalProperties(Properties additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    private Connection doGetConnection(String serverIp, Integer serverPort) {
        Properties props = new Properties();
        if (serverIp != null) {
            props.setProperty(SERVER_IP, serverIp);
        }
        if (serverPort != null) {
            props.setProperty(SERVER_PORT, serverPort + "");
        }
        if (additionalProperties != null) {
            props.putAll(additionalProperties);
        }
        return doGetConnection(props);
    }

    private Connection doGetConnection(Properties properties) {
        ExtensionLoader<Connection> extensionLoader = ExtensionLoaderFactory.getExtensionLoader(Connection.class);
        Connection connection = extensionLoader.getExtension(properties.getProperty(ConnectionConfig.CONNECTION_DRIVER));
        configureConnection(connection);
        connection.connect(properties);
        return connection;
    }

    private void configureConnection(Connection conn) {
        if (defaultNetworkTimeout != null) {
            conn.setNetworkTimeout(Executors.newSingleThreadExecutor(), defaultNetworkTimeout);
        }
    }
}
