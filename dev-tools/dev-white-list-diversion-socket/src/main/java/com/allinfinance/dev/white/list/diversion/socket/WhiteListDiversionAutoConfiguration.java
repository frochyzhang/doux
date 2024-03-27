package com.allinfinance.dev.white.list.diversion.socket;

import com.allinfinance.dev.white.list.diversion.socket.config.WhiteListConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huanghf
 * @date 2024/3/19 11:42
 */
@Configuration
@EnableConfigurationProperties(WhiteListConfig.class)
@ComponentScan(basePackages = "com.allinfinance.dev.white.list.diversion")
public class WhiteListDiversionAutoConfiguration {
}
