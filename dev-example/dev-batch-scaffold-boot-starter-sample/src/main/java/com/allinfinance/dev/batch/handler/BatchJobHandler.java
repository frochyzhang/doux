package com.allinfinance.dev.batch.handler;

import com.allinfinance.dev.batch.scaffold.service.BatchJobService;
import com.allinfinance.dev.dispatch.scaffold.api.IJobHandler;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/24 16:25
 */
@Component
public class BatchJobHandler implements IJobHandler {
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
    public void execute() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, InterruptedException, NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        HashMap<String, JobParameter> pramMap = new HashMap<>();
        JobParameter runTime = new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        pramMap.put("runTime", runTime);
        JobParameters jobParameters = new JobParameters(pramMap);

        batchJobService.startNewJob(testBatchJob, jobParameters);
    }
}
