package com.allinfinance.dev.batch.scaffold.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/25 23:39
 */

public interface BatchJobService {
    /**
     * 发布一个批量任务
     * 注意：同一个任务，创建不同实例时，jobParameters必须不同，
     * 否则会报错“A job execution for this job is already”
     *
     * @param job           任务对象
     * @param jobParameters 任务参数
     * @return JobExecution
     */
    JobExecution startNewJob(Job job, JobParameters jobParameters) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;

    /**
     * 终止一个批量任务
     *
     * @param job           任务对象
     * @param jobParameters 任务参数
     * @return JobExecution
     */
    boolean stopJob(Job job, JobParameters jobParameters) throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException;

    /**
     * 重启一个批量任务
     *
     * @param job           任务对象
     * @param jobParameters 任务参数
     * @return JobExecutionId
     */
    long restartJob(Job job, JobParameters jobParameters) throws NoSuchJobException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, JobParametersInvalidException, JobRestartException;


}
