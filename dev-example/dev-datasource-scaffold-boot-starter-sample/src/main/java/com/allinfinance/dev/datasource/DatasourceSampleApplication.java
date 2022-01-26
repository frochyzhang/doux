package com.allinfinance.dev.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.allinfinance.dev.datasource.scaffold.properties.DruidStatProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/6 10:44
 */
@SpringBootApplication
public class DatasourceSampleApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DatasourceSampleApplication.class, args);

    }
}
