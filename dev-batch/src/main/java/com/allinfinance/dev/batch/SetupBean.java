package com.allinfinance.dev.batch;

import com.allinfinance.dev.batch.mybatis.MapperScanner;
import com.allinfinance.dev.core.constant.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.Map;

/**
 * @author 张勇
 * @description
 * @date 2020/11/30 22:50
 */
@Configuration
@MapperScanner(basePackages = {"${dev.batch.mapper.basePackage}", CommonConstants.DEFAULT_MAPPER_PACKAGE}, sqlSessionFactoryRef = "sqlSessionFactory")
@ImportResource(locations = {"classpath:batch-default-context.xml"})
public class SetupBean {
    private static final Logger logger = LoggerFactory.getLogger(SetupBean.class);

    public SetupBean() {

    }

}
