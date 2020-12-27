package com.allinfinance.dev.batch;

import com.allinfinance.dev.batch.basic.IBasicBatchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author 张勇
 * @description
 * @date 2020/12/4 17:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
public class TestBatch {
    private static final Logger logger = LoggerFactory.getLogger(TestBatch.class);

    @Autowired
    private JobOperator operator;

    @Autowired
    private Job job;

    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private IBasicBatchService basicBatchService;

//    @Before
//    public void setUp() throws Exception {
//        if (!jobRegistry.getJobNames().contains(job.getName())) {
//            jobRegistry.register(new ReferenceJobFactory(job));
//        }
//    }

    @Test
    public void testStartStopResumeJob() throws Exception {
        String params = "run.id=" + 1L;

        Long executionId = basicBatchService.startJob(job.getName(), params);
        assertTrue("Wrong params: " + basicBatchService.getParameters(executionId) + "expected: " + params, basicBatchService.getParameters(executionId).contains(params));
//        stopAndCheckStatus(executionId);

        List<Long> executionIdList = new ArrayList<>();
        executionIdList.add(executionId);
        Thread.sleep(1000);
        Map<Long, Boolean> pauseRet = basicBatchService.pauseJob(executionIdList);

        for (Map.Entry<Long, Boolean> entry : pauseRet.entrySet()) {
            if (entry.getValue()){
                Thread.sleep(1000);
                Long resumedExecutionId = basicBatchService.resumeJob(entry.getKey());
                Thread.sleep(1000);
                assertTrue("Wrong params: " + basicBatchService.getParameters(resumedExecutionId) + "expected: " + params, basicBatchService.getParameters(resumedExecutionId).contains(params));
            }
        }
//
////        long resumedExecutionId = operator.restart(executionId);
////        assertEquals(params, operator.getParameters(resumedExecutionId));
////        stopAndCheckStatus(resumedExecutionId);
////
////        List<Long> instances = operator.getJobInstances(job.getName(), 0, 1);
////        assertEquals(1, instances.size());
////        long instanceId = instances.get(0);
////
////        List<Long> executions = operator.getExecutions(instanceId);
////        assertEquals(2, executions.size());
////        // latest execution is the first in the returned list
////        assertEquals(resumedExecutionId, executions.get(0).longValue());
////        assertEquals(executionId, executions.get(1).longValue());
    }

    /**
     * @param executionId id of running job execution
     */
    private void stopAndCheckStatus(long executionId) throws Exception {
        // wait to the job to get up and running
        Thread.sleep(1000);



//        Set<Long> runningExecutions = operator.getRunningExecutions(job.getName());
//        assertTrue("Wrong executions: " + runningExecutions + " expected: " + executionId, runningExecutions.contains(executionId));
//        assertTrue("Wrong summary: " + operator.getSummary(executionId), operator.getSummary(executionId).contains(BatchStatus.STARTED.toString()));
//
//        operator.stop(executionId);
//
//        int count = 0;
//        while (operator.getRunningExecutions(job.getName()).contains(executionId) && count <= 10) {
//            logger.info("Checking for running JobExecution: count=" + count);
//            Thread.sleep(100);
//            count++;
//        }
//
//        runningExecutions = operator.getRunningExecutions(job.getName());
//        assertFalse("Wrong executions: " + runningExecutions + " expected: " + executionId, runningExecutions.contains(executionId));
//        assertTrue("Wrong summary: " + operator.getSummary(executionId), operator.getSummary(executionId).contains(BatchStatus.STOPPED.toString()));
//
//        // there is just a single step in the test job
//        Map<Long, String> summaries = operator.getStepExecutionSummaries(executionId);
//        logger.info(String.valueOf(summaries));
//        assertTrue(summaries.values().toString().contains(BatchStatus.STOPPED.toString()));
    }
}
