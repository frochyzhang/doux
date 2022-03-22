package com.allinfinance.dev.batch.handler;

import com.allinfinance.dev.batch.scaffold.service.BatchJobService;
import com.allinfinance.dev.batch.scaffold.util.JobParamsUtils;
import com.allinfinance.dev.dispatch.scaffold.api.IJobHandler;
import com.xxl.job.core.context.XxlJobHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/24 16:25
 */
@Component
public class BatchJobHandler implements IJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(BatchJobHandler.class);
    @Autowired
    private Job testBatchJob;
    @Autowired
    private BatchJobService batchJobService;

    /**
     * 设置任务名
     *
     * @return 任务名
     */
    @Override
    public String dispatcherName() {
        return "batchJobHandler";
    }

    /**
     * 任务执行方法
     */
    @Override
    public void execute() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String jobParam = XxlJobHelper.getJobParam();
        logger.info("接收到批量参数：{}", jobParam);
        batchJobService.startNewJob(testBatchJob, JobParamsUtils.parseJobParams(jobParam));
    }
}
