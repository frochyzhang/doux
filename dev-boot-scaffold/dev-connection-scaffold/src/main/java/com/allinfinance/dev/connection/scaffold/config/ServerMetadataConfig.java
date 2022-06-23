package com.allinfinance.dev.connection.scaffold.config;

import com.allinfinance.dev.connection.scaffold.metadata.ServerMetadata;
import com.allinfinance.dev.connection.scaffold.pool.PooledServerMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * 服务端参数列表
     */
    private Map<String, ServerMetadata> serverMetadataMap;

    public Map<String, ServerMetadata> getServerMetadataMap() {
        return serverMetadataMap;
    }

    public void setServerMetadataMap(Map<String, ServerMetadata> serverMetadataMap) {
        this.serverMetadataMap = serverMetadataMap;
    }

    @Bean(name = "pooledServerMetadataList")
    public List<PooledServerMetadata> getPooledServerMetadataList() {
        return serverMetadataMap.values()
                .stream().map(serverMetadata -> {
                    logger.info("服务端配置信息：{}", serverMetadata);
                    PooledServerMetadata pooledServerMetadata = new PooledServerMetadata(serverMetadata);
                    try {
                        pooledServerMetadata.init();
                    } catch (Throwable e) {
                        logger.error("初始化连接异常，请检查服务端！", e);
                    }
                    return pooledServerMetadata;
                }).collect(Collectors.toList());
    }

    @Bean(name = "queueServerMetadataList")
    public List<QueueServerMetadata> getQueueServerMetadataList() {
        return serverMetadataMap.values()
                .stream().map(serverMetadata -> {
                    logger.info("服务端配置信息：{}", serverMetadata);
                    QueueServerMetadata pooledServerMetadata = new QueueServerMetadata(serverMetadata);
                    try {
                        pooledServerMetadata.init();
                    } catch (Throwable e) {
                        logger.error("初始化连接异常，请检查服务端！", e);
                    }
                    return pooledServerMetadata;
                }).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ServerMetadataConfig{" +
                "serverMetadataMap=" + serverMetadataMap +
                '}';
    }

}
