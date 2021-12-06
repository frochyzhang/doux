package com.allinfinance.dev.batch.autoconfigure;

import com.allinfinance.dev.core.constant.CommonConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author 张勇
 * @description
 * @date 2020/11/30 22:50
 */
@Configuration
@MapperScan(basePackages = {CommonConstants.DEFAULT_MAPPER_PACKAGE}, sqlSessionFactoryRef = "sqlSessionFactory")
@ImportResource(locations = {"classpath:batch-default-context.xml"})
public class SetupBean {
    private static final Logger logger = LoggerFactory.getLogger(SetupBean.class);

    public SetupBean() {
        logger.info("初始化batch连接参数！");
    }
}
