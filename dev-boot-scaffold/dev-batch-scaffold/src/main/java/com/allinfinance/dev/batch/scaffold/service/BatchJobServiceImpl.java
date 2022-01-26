package com.allinfinance.dev.batch.scaffold.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/24 15:25
 */
@Component
public class BatchJobServiceImpl implements BatchJobService {
    private static final Logger logger = LoggerFactory.getLogger(BatchJobServiceImpl.class);
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobOperator jobOperator;

    /**
     * 发布一个批量任务
     *
     * @param job           任务对象
     * @param jobParameters 任务参数
     * @return JobExecution
     */
    @Override
    public JobExecution startNewJob(Job job, JobParameters jobParameters) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        logger.info("创建新的批量任务，任务名：{}", job.getName());
        return jobLauncher.run(job, jobParameters);
    }

    /**
     * 终止一个批量任务
     *
     * @param job           任务对象
     * @param jobParameters 任务参数
     * @return JobExecution
     */
    @Override
    public boolean stopJob(Job job, JobParameters jobParameters) throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        Set<Long> executions = jobOperator.getRunningExecutions(job.getName());
        return jobOperator.stop(executions.iterator().next());
    }

    /**
     * 重启一个批量任务
     *
     * @param job           任务对象
     * @param jobParameters 任务参数
     * @return JobExecution
     */
    @Override
    public long restartJob(Job job, JobParameters jobParameters) throws NoSuchJobException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, JobParametersInvalidException, JobRestartException {
        Set<Long> executions = jobOperator.getRunningExecutions(job.getName());
        return jobOperator.restart(executions.iterator().next());
    }


}
