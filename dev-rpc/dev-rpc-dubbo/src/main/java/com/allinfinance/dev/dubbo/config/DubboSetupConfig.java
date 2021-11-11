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
        logger.warn("默认使用zookeeper作为注册中心,若要使用nacos, 需引入 " +
                "           <dependency>\n" +
                "                <groupId>org.apache.dubbo</groupId>\n" +
                "                <artifactId>dubbo-registry-nacos</artifactId>\n" +
                "            </dependency>");
        logger.info("初始化Dubbo连接参数！");
    }
}
