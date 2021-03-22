package com.allinfinance.dev.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"classpath:dev-config-autoconfigure.xml"})
public class AutoConfigSetup {
    private static final Logger logger = LoggerFactory.getLogger(AutoConfigSetup.class);

    public AutoConfigSetup() {
        logger.info("自动装配配置文件中!");
    }
}
