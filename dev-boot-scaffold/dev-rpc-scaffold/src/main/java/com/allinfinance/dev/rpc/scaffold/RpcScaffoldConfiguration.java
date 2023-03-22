package com.allinfinance.dev.rpc.scaffold;

import cn.hutool.extra.spring.SpringUtil;
import com.allinfinance.dev.rpc.scaffold.advice.resolver.ExceptionHandlerExceptionResolver;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author qipeng
 * @date 2021/12/30 17:49
 */
@EnableConfigurationProperties(RpcConfigurationProperties.class)
@Configuration
@ComponentScan(basePackages = RpcScaffoldConfiguration.SCAFFOLD_BASE_PACKAGE_PREFIX)
public class RpcScaffoldConfiguration {
    public static final String SCAFFOLD_BASE_PACKAGE_PREFIX = "com.allinfinance.dev.rpc.scaffold";
    private static final Logger logger = LoggerFactory.getLogger(RpcScaffoldConfiguration.class);

    public RpcScaffoldConfiguration() {
        logger.info("RPC脚手架加载成功!");
    }

    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        RpcConfigurationProperties properties = SpringUtil.getBean(RpcConfigurationProperties.class);
        if (properties != null && properties.getProvider() != null) {
            return new ExceptionHandlerExceptionResolver();
        }
        return null;
    }
}
