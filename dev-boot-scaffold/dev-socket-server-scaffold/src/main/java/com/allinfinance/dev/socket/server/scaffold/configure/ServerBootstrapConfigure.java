package com.allinfinance.dev.socket.server.scaffold.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-23 13:44
 */
@Configuration
@ConfigurationProperties("com.allinfinance.socket.server.bootstrap")
public class ServerBootstrapConfigure {
    /**
     * 是否启动端口
     */
    private Boolean serverEnabled;
    /**
     * 服务端底层实现类：default
     */
    private String bootstrap;

    public ServerBootstrapConfigure() {
    }

    public String getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
    }

    public Boolean getServerEnabled() {
        return serverEnabled;
    }

    public void setServerEnabled(Boolean serverEnabled) {
        this.serverEnabled = serverEnabled;
    }

    @Override
    public String toString() {
        return "ServerBootstrapConfigure{" +
                "bootstrap='" + bootstrap + '\'' +
                '}';
    }
}
