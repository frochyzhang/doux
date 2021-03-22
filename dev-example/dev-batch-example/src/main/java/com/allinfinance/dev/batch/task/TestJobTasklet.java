package com.allinfinance.dev.batch.task;

import com.allinfinance.dev.batch.basic.IBasicBatchService;
import com.allinfinance.dev.batch.dao.service.JobExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 张勇
 * @description
 * @date 2020/12/5 19:54
 */
public class TestJobTasklet implements Tasklet {
    private static final Logger logger = LoggerFactory.getLogger(TestJobTasklet.class);

    private String sysOrgId;

    @Autowired
    private JobOperator jobOperator;
    @Autowired
    private IBasicBatchService basicBatchService;
    @Autowired
    private JobExecutionService jobExecutionService;


    public void setSysOrgId(String sysOrgId) {
        this.sysOrgId = sysOrgId;
    }

    public String getSysOrgId() {
        return sysOrgId;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        logger.info("机构{}的测试任务!", sysOrgId);

        List<Long> runningExecution = basicBatchService.getRunningExecution(stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName());
        basicBatchService.getJobSummaryInfo(runningExecution).forEach(jobSummaryInfo -> logger.info("summaryInfo:{}", jobSummaryInfo));
        return RepeatStatus.FINISHED;
    }
}
