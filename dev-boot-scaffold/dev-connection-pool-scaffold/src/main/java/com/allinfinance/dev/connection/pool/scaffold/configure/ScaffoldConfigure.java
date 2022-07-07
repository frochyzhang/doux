package com.allinfinance.dev.connection.pool.scaffold.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/7
 **/
@Configuration
@ConfigurationProperties(prefix = "com.allinfinance.connection.scaffold")
public class ScaffoldConfigure {
    /**
     * 服务端参数列表
     */
    private Map<String, ServerMetadataConfigure> serverMetadataMap;

    public Map<String, ServerMetadataConfigure> getServerMetadataMap() {
        return serverMetadataMap;
    }

    public void setServerMetadataMap(Map<String, ServerMetadataConfigure> serverMetadataMap) {
        this.serverMetadataMap = serverMetadataMap;
    }

    @Override
    public String toString() {
        return "ScaffoldConfigure{" +
                "serverMetadataMap=" + serverMetadataMap +
                '}';
    }
}
