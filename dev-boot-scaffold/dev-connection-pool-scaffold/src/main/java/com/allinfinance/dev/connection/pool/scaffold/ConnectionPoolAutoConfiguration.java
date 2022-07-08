package com.allinfinance.dev.connection.pool.scaffold;

import com.allinfinance.dev.connection.pool.scaffold.configure.ConnectionPoolConfigure;
import com.allinfinance.dev.connection.pool.scaffold.configure.ScaffoldConfigure;
import com.allinfinance.dev.connection.pool.scaffold.configure.ServerMetadataConfigure;
import com.allinfinance.dev.connection.pool.scaffold.util.PropertiesParseUtils;
import com.allinfinance.dev.framework.conn.driver.ServerMetadata;
import com.allinfinance.dev.framework.conn.driver.ServerMetadataFactory;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/7
 **/
@EnableConfigurationProperties({ConnectionPoolConfigure.class,ScaffoldConfigure.class})
@Configuration
@ComponentScan("com.allinfinance.dev.connection.pool.scaffold")
public class ConnectionPoolAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPoolAutoConfiguration.class);

    @Autowired
    private ScaffoldConfigure scaffoldConfigure;
    @Autowired
    private ConnectionPoolConfigure connectionPoolConfigure;

    @Bean(name = "serverMetadataList")
    public List<ServerMetadata> getServerMetadataList() {
        return scaffoldConfigure.getServerMetadataMap().values()
                .stream().map(metadataConfigure -> {
                    logger.info("服务端配置信息：{}", metadataConfigure);

                    Properties properties = PropertiesParseUtils.fromServerMetadataConfigure(metadataConfigure);
                    ExtensionLoader<ServerMetadataFactory> serverMetadataExtensionLoader = ExtensionLoaderFactory.getExtensionLoader(ServerMetadataFactory.class);
                    ServerMetadataFactory factory = serverMetadataExtensionLoader.getExtension(connectionPoolConfigure.connectionPoolType);

                    factory.setProperties(properties);
                    return factory.getMetadata();
                }).collect(Collectors.toList());
    }
}