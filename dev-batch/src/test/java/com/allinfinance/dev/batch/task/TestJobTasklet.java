package com.allinfinance.dev.batch.task;

import com.allinfinance.dev.batch.basic.IBasicBatchService;
import com.allinfinance.dev.batch.service.TblBatCtlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

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
    private TblBatCtlService tblBatCtlService;
    @Autowired
    private IBasicBatchService basicBatchService;


    public void setSysOrgId(String sysOrgId) {
        this.sysOrgId = sysOrgId;
    }

    public String getSysOrgId() {
        return sysOrgId;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        logger.info("机构{}的测试任务!", sysOrgId);
//        for (Long aLong : jobOperator.getRunningExecutions(stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName())) {
//            jobOperator.stop(aLong);
//        }
        for (String summaryInfo : basicBatchService.getJobSummaryInfo(stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName())) {
            logger.info("summaryInfo: {}",summaryInfo);

        }
        return RepeatStatus.FINISHED;
    }
}
