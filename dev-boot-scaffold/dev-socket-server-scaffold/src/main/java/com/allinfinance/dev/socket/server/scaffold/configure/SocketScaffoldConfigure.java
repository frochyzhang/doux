package com.allinfinance.dev.socket.server.scaffold.configure;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-22 13:40
 */
@Configuration
@ConfigurationProperties(prefix = "com.allinfinance.socket.server.scaffold")
public class SocketScaffoldConfigure {
    /**
     * Socket服务端参数列表
     */
    private List<ServerMetadataConfigure> serverMetadataList;

    public void setServerMetadataList(List<ServerMetadataConfigure> serverMetadataList) {
        this.serverMetadataList = serverMetadataList;
    }

    public List<ServerMetadataConfigure> getServerMetadataList() {
        return serverMetadataList;
    }

    @Override
    public String toString() {
        return "SocketScaffoldConfigure{" +
                "serverMetadataMap=" + serverMetadataList +
                '}';
    }
}
