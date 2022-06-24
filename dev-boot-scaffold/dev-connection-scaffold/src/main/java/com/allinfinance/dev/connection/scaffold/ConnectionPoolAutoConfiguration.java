package com.allinfinance.dev.connection.scaffold;

import com.allinfinance.dev.connection.scaffold.config.ServerMetadataConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author qipeng
 * @date 2022/6/16 10:51
 * @description
 */
@EnableConfigurationProperties(ServerMetadataConfig.class)
@Configuration
@ComponentScan("com.allinfinance.dev.connection.scaffold")
public class ConnectionPoolAutoConfiguration {
}
