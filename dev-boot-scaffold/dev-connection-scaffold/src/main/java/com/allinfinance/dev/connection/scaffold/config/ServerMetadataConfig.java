package com.allinfinance.dev.connection.scaffold.config;

import com.allinfinance.dev.connection.scaffold.config.constant.ConnectionPoolType;
import com.allinfinance.dev.connection.scaffold.metadata.ServerMetadata;
import com.allinfinance.dev.connection.scaffold.pool.PooledServerMetadata;
import com.allinfinance.dev.connection.scaffold.pool.QueueServerMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qipeng
 * @date 2022/6/16 10:06
 */
@Configuration
@ConfigurationProperties(prefix = "com.allinfinance.connection")
public class ServerMetadataConfig {
    private static final Logger logger = LoggerFactory.getLogger(ServerMetadataConfig.class);
    /**
     * 连接池类型：list或queue
     */
    private String poolType;
    /**
     * 接收缓冲区大小，默认2KB
     */
    private Integer bufferSize = 2048;
    /**
     * 报文长度域，默认无长度域
     */
    private Integer lengthField = 0;
    /**
     * 服务端参数列表
     */
    private Map<String, ServerMetadata> serverMetadataMap;

    public Map<String, ServerMetadata> getServerMetadataMap() {
        return serverMetadataMap;
    }

    public void setServerMetadataMap(Map<String, ServerMetadata> serverMetadataMap) {
        this.serverMetadataMap = serverMetadataMap;
    }

    @ConditionalOnProperty(value = "com.allinfinance.connection.pool-type", havingValue = ConnectionPoolType.LIST)
    @Bean(name = "pooledServerMetadataList")
    public List<PooledServerMetadata> getPooledServerMetadataList() {
        return serverMetadataMap.values()
                .stream().map(serverMetadata -> {
                    logger.info("服务端配置信息：{}", serverMetadata);
                    PooledServerMetadata pooledServerMetadata = new PooledServerMetadata(serverMetadata, bufferSize, lengthField);
                    try {
                        pooledServerMetadata.init();
                    } catch (Throwable e) {
                        logger.error("初始化连接异常，请检查服务端！", e);
                    }
                    return pooledServerMetadata;
                }).collect(Collectors.toList());
    }

    @ConditionalOnProperty(value = "com.allinfinance.connection.pool-type", havingValue = ConnectionPoolType.QUEUE)
    @Bean(name = "queueServerMetadataList")
    public List<QueueServerMetadata> getQueueServerMetadataList() {
        return serverMetadataMap.values()
                .stream().map(serverMetadata -> {
                    logger.info("服务端配置信息：{}", serverMetadata);
                    QueueServerMetadata pooledServerMetadata = new QueueServerMetadata(serverMetadata, bufferSize, lengthField);
                    try {
                        pooledServerMetadata.init();
                    } catch (Throwable e) {
                        logger.error("初始化连接异常，请检查服务端！", e);
                    }
                    return pooledServerMetadata;
                }).collect(Collectors.toList());
    }

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Integer getLengthField() {
        return lengthField;
    }

    public void setLengthField(Integer lengthField) {
        this.lengthField = lengthField;
    }

    @Override
    public String toString() {
        return "ServerMetadataConfig{" +
                "poolType='" + poolType + '\'' +
                ", bufferSize=" + bufferSize +
                ", lengthField=" + lengthField +
                ", serverMetadataMap=" + serverMetadataMap +
                '}';
    }
}
