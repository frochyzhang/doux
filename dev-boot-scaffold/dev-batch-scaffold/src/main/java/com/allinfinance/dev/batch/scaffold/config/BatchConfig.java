package com.allinfinance.dev.batch.scaffold.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author qipeng
 * @date 2022/2/10 18:39
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);
    @Autowired
    private JobRepository jobRepository;

    @Bean("devJobLauncher")
    @Primary
    @ConditionalOnProperty(prefix = "com.allinfinance.batch", name = "async", havingValue = "true", matchIfMissing = true)
    public JobLauncher jobLauncher() {
        logger.info("批量异步执行开关开启，如需关闭请将com.allinfinance.batch.async置为false！");
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        taskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        taskExecutor.setKeepAliveSeconds(0);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("dev-batch-exec-pool-");
        taskExecutor.initialize();
        launcher.setTaskExecutor(taskExecutor);
        return launcher;
    }
}
