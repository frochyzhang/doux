package com.allinfinance.dev.batch.scaffold.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author qipeng
 * @date 2022/2/10 18:39
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public BatchConfigurer batchConfigurer() {
        return new DefaultBatchConfigurer(dataSource);
    }
}
