package com.allinfinance.dev.datasource.scaffold.config;

import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author qipeng
 * @date 2022/1/6 9:45
 */
@Configuration
public class DevMybatisProperties {

    @Primary
    @ConfigurationProperties(prefix = "com.allinfinance.datasource.mybatis")
    @Bean("mybatisProperties")
    public MybatisProperties fillMybatisProperties() {
        return new MybatisProperties();
    }
}
