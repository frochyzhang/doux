package com.allinfinance.dev.batch.scaffold.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/1/25 23:14
 */
@Component
public class SimpleJobListener implements JobExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(SimpleJobListener.class);

    /**
     * Callback before a job executes.
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Job开始执行之前，可进行一些参数校验...");
        logger.info("Job参数：" + jobExecution.getJobParameters());
    }

    /**
     * Callback after completion of a job. Called after both both successful and
     * failed executions. To perform logic on a particular status, use
     * "if (jobExecution.getStatus() == BatchStatus.X)".
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("Job执行成功，生成.OK文件...");
        }
    }
}
