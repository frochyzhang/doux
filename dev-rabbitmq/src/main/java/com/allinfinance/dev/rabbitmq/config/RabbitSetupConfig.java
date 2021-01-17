package com.allinfinance.dev.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

// TODO: 2021/1/17 若出现connectionFactory bean找不到的情况，请添加scanBasePackages
@Configuration
@ImportResource(locations = {"classpath:dev-rabbitmq-config-default.xml"})
public class RabbitSetupConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitSetupConfig.class);

    public RabbitSetupConfig() {
        logger.info("初始化rabbitMq初始化连接参数。");
    }
}
