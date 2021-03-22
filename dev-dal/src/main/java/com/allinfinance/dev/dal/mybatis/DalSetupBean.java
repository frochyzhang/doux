package com.allinfinance.dev.dal.mybatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"classpath:dev-dal-default-config.xml"})
public class DalSetupBean {

    private static final Logger logger = LoggerFactory.getLogger(DalSetupBean.class);

    public DalSetupBean() {
        logger.info("Dal模块初始化加载!");
    }
}
