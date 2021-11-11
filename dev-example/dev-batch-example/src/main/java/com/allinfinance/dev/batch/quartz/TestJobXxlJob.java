package com.allinfinance.dev.batch.quartz;

import com.allinfinance.dev.batch.basic.IBasicBatchService;
import com.allinfinance.dev.batch.batch.BatchParameterHelper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.HashMap;

/**
 * @author 张勇
 * @description
 * @date 2020/12/5 19:58
 */
@Component
public class TestJobXxlJob {
    private static final Logger logger = LoggerFactory.getLogger(TestJobXxlJob.class);


    @Autowired
    private IBasicBatchService iBasicBatchService;
    @Autowired
    private org.springframework.batch.core.Job testJob;
    @Autowired
    private org.springframework.batch.core.Job readFileJob;

    private static Long counter = 0L;

    @XxlJob("testJobHandler")
    public void execute() {
        logger.info("testJob start...");
        StopWatch sw = new StopWatch();
        sw.start();

        try {
            HashMap<String, String> jobParams = BatchParameterHelper.getXxlParameters();
//            jobParams.put("sysOrgId", "sysOrgId");
//
//            jobParams.put("filePath", "Z:/zy/workspace/git/dev-latest/images/STMT_000064500000__E");
//            jobParams.put("gridSize", "10");
//            jobParams.put("startLine", "1");
            XxlJobHelper.log("jobParams:{}", jobParams.toString());

//
//            List<BatchJobDto> jobInfoStoppedFailed = iBasicBatchService.getResumableJob();
//            logger.info("wait....");
//
//            List<Long> ids = new ArrayList<>();
//
//            for (BatchJobDto batchJobDto : jobInfoStoppedFailed) {
////                iBasicBatchService.resumeBatch(batchJobDto.getJobExecutionId());
//                ids.add(batchJobDto.getJobExecutionId());
//            }
//
//            List<Long> pauseJobRet = iBasicBatchService.resumeJob(ids);
//
//            logger.info("done...");

//            iBasicBatchService.startJob("readFileJob", "sysOrgId=9900000,gridSize=10,startLine=1,filePath=Z:/zy/workspace/git/dev-latest/images/STMT_000064500000__E");
            JobExecution je = iBasicBatchService.startNewBatch(readFileJob, jobParams);
////            JobExecution je = iBasicBatchService.startNewBatch(testJob, jobParams);
//            if (null != je && !je.getStatus().isUnsuccessful()) {
//                logger.info("批量执行成功: {}", testJob.getName());
//            } else {
//                logger.error("批量执行失败: {}", testJob.getName());
//            }
        } catch (Exception e) {
            logger.error("批量执行异常!", e);
        }

        sw.stop();
        XxlJobHelper.log("Time elapsed:{},Execute quartz ledgerJob:{}", sw.prettyPrint(), ++counter);
    }
}
