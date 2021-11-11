package com.allinfinance.dev.hsp.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;

/**
 * Classname  com.allinfinance.dev.hsp.config.HspInitConfiguration
 *
 * @Description TODO
 * @Date 2021/3/25 16:10
 * @Created by ZhangYong
 */
@Slf4j
@Configuration
@EnableCaching
@MapperScan(basePackages = {"com.allinfinance.dev.hsp.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory")
@ImportResource(locations = {"classpath:hsp-rpc-context.xml"})
public class HspInitConfiguration {

    public HspInitConfiguration() {
        log.info("dev-hsp初始化!");
    }

    @Primary
    @Bean("keyCacheManager")
    public CacheManager keyCacheManager() {
        return new ConcurrentMapCacheManager("keyCache");
    }

    @Bean("zmkIndexCacheManager")
    public CacheManager zmkIndexCacheManager() {
        return new ConcurrentMapCacheManager("zmkIndexCache");
    }
}
