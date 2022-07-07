package com.allinfinance.dev.connection.pool.scaffold;

import com.allinfinance.dev.connection.pool.scaffold.configure.ServerMetadataConfigure;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/7
 **/
@EnableConfigurationProperties(ServerMetadataConfigure.class)
@Configuration
@ComponentScan("com.allinfinance.dev")
public class ConnectionPoolAutoConfiguration {
}