package com.allinfinance.dev.dubbo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

//"classpath:dev-config-autoconfigure.xml",
@Configuration
@ImportResource(locations = {"classpath:dev-dubbo-config-default.xml"})
public class DubboSetupConfig {

    private static final Logger logger = LoggerFactory.getLogger(DubboSetupConfig.class);

    public DubboSetupConfig() {
        logger.info("初始化Dubbo连接参数！");
    }
}
