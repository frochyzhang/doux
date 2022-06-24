package com.allinfinance.dev.connection.scaffold;

import com.allinfinance.dev.connection.scaffold.config.ServerMetadataConfig;
import com.allinfinance.dev.connection.scaffold.config.constant.ConnectionPoolType;
import com.allinfinance.dev.connection.scaffold.pool.PooledServerMetadata;
import com.allinfinance.dev.connection.scaffold.pool.QueueServerMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qipeng
 * @date 2022/6/16 10:51
 * @description
 */
@EnableConfigurationProperties(ServerMetadataConfig.class)
@Configuration
@ComponentScan(basePackages = "com.allinfinance.dev.connection.scaffold")
public class ConnectionPoolAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPoolAutoConfiguration.class);
    @Autowired
    private ServerMetadataConfig serverMetadataConfig;

    @ConditionalOnProperty(value = "com.allinfinance.connection.pool-type", havingValue = ConnectionPoolType.LIST)
    @Bean(name = "pooledServerMetadataList")
    public List<PooledServerMetadata> getPooledServerMetadataList() {
        return serverMetadataConfig.getServerMetadataMap().values()
                .stream().map(serverMetadata -> {
                    logger.info("服务端配置信息：{}", serverMetadata);
                    PooledServerMetadata pooledServerMetadata = new PooledServerMetadata(serverMetadata, serverMetadataConfig.getBufferSize());
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
        return serverMetadataConfig.getServerMetadataMap().values()
                .stream().map(serverMetadata -> {
                    logger.info("服务端配置信息：{}", serverMetadata);
                    QueueServerMetadata pooledServerMetadata = new QueueServerMetadata(serverMetadata, serverMetadataConfig.getBufferSize());
                    try {
                        pooledServerMetadata.init();
                    } catch (Throwable e) {
                        logger.error("初始化连接异常，请检查服务端！", e);
                    }
                    return pooledServerMetadata;
                }).collect(Collectors.toList());
    }
}
