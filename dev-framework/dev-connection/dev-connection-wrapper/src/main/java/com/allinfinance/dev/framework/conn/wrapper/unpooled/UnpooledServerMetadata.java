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

import com.allinfinance.dev.framework.conn.driver.Connection;
import com.allinfinance.dev.framework.conn.driver.ServerMetadata;
import com.allinfinance.dev.framework.conn.wrapper.manager.DriverManager;

import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public class UnpooledServerMetadata implements ServerMetadata {

    private Integer defaultNetworkTimeout;

    private String serverIp;

    private Integer serverPort;

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

    /**
     * Gets the default network timeout.
     *
     * @return the default network timeout
     * @since 3.5.2
     */
    public Integer getDefaultNetworkTimeout() {
        return defaultNetworkTimeout;
    }

    /**
     * Sets the default network timeout value to wait for the database operation to complete. See {@link Connection#setNetworkTimeout(java.util.concurrent.Executor, int)}
     *
     * @param defaultNetworkTimeout The time in milliseconds to wait for the database operation to complete.
     * @since 3.5.2
     */
    public void setDefaultNetworkTimeout(Integer defaultNetworkTimeout) {
        this.defaultNetworkTimeout = defaultNetworkTimeout;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    private Connection doGetConnection(String serverIp, Integer serverPort) {
        Properties props = new Properties();
        if (serverIp != null) {
            props.setProperty("serverIp", serverIp);
        }
        if (serverPort != null) {
            props.setProperty("serverPort", serverPort + "");
        }
        return doGetConnection(props);
    }

    private Connection doGetConnection(Properties properties) {
        Connection connection = DriverManager.getConnection(properties);
        configureConnection(connection);
        return connection;
    }

    private void configureConnection(Connection conn) {
        if (defaultNetworkTimeout != null) {
            conn.setNetworkTimeout(Executors.newSingleThreadExecutor(), defaultNetworkTimeout);
        }
    }
}
