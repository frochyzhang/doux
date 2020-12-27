//package com.allinfinance.dev.batch.quartz;
//
//import com.allinfinance.dev.batch.basic.IBasicBatchService;
//import com.allinfinance.dev.core.bean.BatchJobDto;
//import org.apache.commons.lang3.StringUtils;
//import org.quartz.Job;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StopWatch;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @author 张勇
// * @description
// * @date 2020/12/5 19:58
// */
//@Component("testCronJob")
//public class TestJobQuartz implements Job {
//    private static final Logger logger = LoggerFactory.getLogger(TestJobQuartz.class);
//
//    private static final String RUN_MONTH_KEY = "run.month";
//
//    @Autowired
//    private IBasicBatchService iBasicBatchService;
//    @Autowired
//    private org.springframework.batch.core.Job testJob;
//    @Autowired
//    private org.springframework.batch.core.Job readFileJob;
//
//    private static Long counter = 0L;
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        logger.info("testJob start...");
//        StopWatch sw = new StopWatch();
//        sw.start();
//
//        try {
//            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
//            HashMap<String, String> jobParams = new HashMap<>();
//            jobParams.put(RUN_MONTH_KEY, String.valueOf(System.currentTimeMillis()));
//            String sysOrgId = jobDataMap.getString("sysOrgId");
//            if (StringUtils.isEmpty(sysOrgId)) {
//                logger.error("机构号不存在!");
//                return;
//            }
//
//            jobParams.put("sysOrgId", sysOrgId);
//            logger.info("jobParams:{}", jobParams.toString());
//
//            jobParams.put("run.month", String.valueOf(System.currentTimeMillis()));
//            jobParams.put("filePath", "/Users/zhangyong/workspace/allinfinance/dev-copy/STMT_000064500000__E");
//            jobParams.put("gridSize","10");
//            jobParams.put("startLine","1");
////
////            List<BatchJobDto> jobInfoStoppedFailed = iBasicBatchService.getResumableJob();
////            logger.info("wait....");
////
////            List<Long> ids = new ArrayList<>();
////
////            for (BatchJobDto batchJobDto : jobInfoStoppedFailed) {
//////                iBasicBatchService.resumeBatch(batchJobDto.getJobExecutionId());
////                ids.add(batchJobDto.getJobExecutionId());
////            }
////
////            List<Long> pauseJobRet = iBasicBatchService.resumeJob(ids);
////
////            logger.info("done...");
//
//            iBasicBatchService.startJob("readFileJob", "sysOrgId=9900000,gridSize=10,startLine=1,filePath=/Users/zhangyong/workspace/allinfinance/dev-copy/STMT_000064500000__E");
////            JobExecution je = iBasicBatchService.startNewBatch(readFileJob, jobParams);
//////            JobExecution je = iBasicBatchService.startNewBatch(testJob, jobParams);
////            if (null != je && !je.getStatus().isUnsuccessful()) {
////                logger.info("批量执行成功: {}", testJob.getName());
////            } else {
////                logger.error("批量执行失败: {}", testJob.getName());
////            }
//        } catch (Exception e) {
//            logger.error("批量执行异常!", e);
//        }
//
//        sw.stop();
//        logger.info("Time elapsed:{},Execute quartz ledgerJob:{}", sw.prettyPrint(), ++counter);
//    }
//}
