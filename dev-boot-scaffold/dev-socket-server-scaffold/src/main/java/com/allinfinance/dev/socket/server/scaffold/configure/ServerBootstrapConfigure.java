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
    private Boolean enabled;

    public ServerBootstrapConfigure() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "ServerBootstrapConfigure{" +
                "enabled=" + enabled +
                '}';
    }
}
