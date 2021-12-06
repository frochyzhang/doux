package com.allinfinance.dev.batch.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/12/6 10:37
 */
@Configuration
@ImportResource(locations = {"classpath:batch-quartz-default-job.xml"})
public class QuartzJobAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(QuartzJobAutoConfiguration.class);

    public QuartzJobAutoConfiguration() {
        logger.info("初始化Quartz基础bean！");
    }
}
