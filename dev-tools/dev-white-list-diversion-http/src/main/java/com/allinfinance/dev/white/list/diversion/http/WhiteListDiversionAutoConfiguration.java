package com.allinfinance.dev.white.list.diversion.http;

import com.allinfinance.dev.white.list.diversion.http.config.WhiteListConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huanghf
 * @date 2024/3/19 11:42
 */
@Configuration
@EnableConfigurationProperties(WhiteListConfig.class)
@ComponentScan(basePackages = "com.allinfinance.dev.white.list.diversion.http")
public class WhiteListDiversionAutoConfiguration {
}
