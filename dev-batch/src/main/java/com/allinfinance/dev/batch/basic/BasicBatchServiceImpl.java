package com.allinfinance.dev.batch.basic;

import com.allinfinance.dev.batch.service.TblBatCtlService;
import com.allinfinance.dev.core.bean.BatchJobDto;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BasicBatchServiceImpl
 *
 * @author hongmr
 * @date 2017/1/6
 */
public class BasicBatchServiceImpl implements IBasicBatchService {
    private SimpleJobLauncher launcher;

    private static final Logger logger = LoggerFactory.getLogger(BasicBatchServiceImpl.class);

    @Autowired
    private JobOperator jobOperator;
    @Autowired
    private TblBatCtlService tblBatCtlService;

    /**
     * 启动SpringBatch Job
     *
     * @param job       任务
     * @param jobParams 任务参数
     * @return 任务执行结果
     */
    @Override
    public JobExecution startNewBatch(Job job, HashMap<String, String> jobParams) {
        JobExecution je = null;
        try {
            Map<String, JobParameter> parameters = new HashMap<>(16);
            if (jobParams != null && jobParams.size() > 0) {
                for (Map.Entry<String, String> entry : jobParams.entrySet()) {
                    parameters.put(entry.getKey(), new JobParameter(entry.getValue()));
                }
                je = launcher.run(job, new JobParameters(parameters));
            }
        } catch (Exception e) {
            logger.error("执行批量job发生异常：" + job.getName());
            logger.error("异常信息:", e);
        }
        return je;
    }

    @Override
    public Long startJob(String jobName, String parameters) throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {
        logger.info("新增任务名：{}, 参数信息：{}", jobName, parameters);
        return jobOperator.start(jobName, parameters);
    }

    @Override
    public Map<Long, Boolean> pauseJob(List<Long> executionIdList) {
        Map<Long, Boolean> pauseResult = new HashMap<>();
        try {
            for (Long runningExecution : executionIdList) {
                logger.info("任务执行概要信息:{}", jobOperator.getSummary(runningExecution));
                boolean stopRet = jobOperator.stop(runningExecution);
                if (stopRet) {
                    logger.info("任务已停止执行!{}", runningExecution);
                } else {
                    logger.error("停止任务失败!{}", runningExecution);
                }
                pauseResult.put(runningExecution, stopRet);
            }
        } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
            logger.error("任务不存在,请检查任务是否正确!", e);
        }
        return pauseResult;
    }

    @Override
    public Long resumeJob(Long jobExecutionId) throws JobInstanceAlreadyCompleteException {

        if (null != tblBatCtlService.selectCompletedJobExecution(jobExecutionId)) {
            logger.info("该失败批量已重跑，无需再次重跑!{}", jobExecutionId);
            tblBatCtlService.deleteCompletedJob(jobExecutionId);
            throw new JobInstanceAlreadyCompleteException("该失败批量已重跑，无需再次重跑!");
        }
        Long ret = 0L;
        try {
            logger.info("重新启动批量：{}", jobExecutionId);
            ret = jobOperator.restart(jobExecutionId);
        } catch (JobInstanceAlreadyCompleteException | NoSuchJobException e) {
            logger.error("批量任务已完成，无需重启!", e);
        } catch (NoSuchJobExecutionException e) {
            logger.error("{}不存在,请检查任务号是否正确!", jobExecutionId, e);
        } catch (JobRestartException e) {
            logger.error("任务重启失败!", e);
        } catch (JobParametersInvalidException e) {
            logger.error("任务参数非法!", e);
        }
        return ret;
    }

    @Override
    public List<Long> resumeJob(String jobName) throws JobInstanceAlreadyCompleteException {
        List<Long> rets = new ArrayList<>();
        try {
            for (Long runningExecution : jobOperator.getRunningExecutions(jobName)) {
                rets.add(resumeJob(runningExecution));
            }
        } catch (NoSuchJobException e) {
            logger.error("{}不存在,请检查任务名是否正确!", jobName, e);
        }
        return rets;
    }

    @Override
    public List<Long> resumeJob(List<Long> jobExecutionIds) throws JobInstanceAlreadyCompleteException {
        List<Long> rets = new ArrayList<>();
        for (Long jobExecutionId : jobExecutionIds) {
            rets.add(resumeJob(jobExecutionId));
        }
        return rets;
    }

    @Override
    public List<Long> abandonJob(List<Long> jobExecutionIds) {
        for (Map.Entry<Long, Boolean> pauseRet : pauseJob(jobExecutionIds).entrySet()) {
            if (pauseRet.getValue()) {
                try {
                    jobOperator.abandon(pauseRet.getKey());
                } catch (NoSuchJobExecutionException | JobExecutionAlreadyRunningException e) {
                    logger.error("abandon Job处理异常!", e);
                }
            }
        }
        return jobExecutionIds;
    }


    @Override
    public List<String> getJobSummaryInfo(String jobName) {
        List<String> summaryInfoList = new ArrayList<>();
        try {
            for (Long runningExecution : jobOperator.getRunningExecutions(jobName)) {
                String summaryInfo = jobOperator.getSummary(runningExecution);
                logger.info("任务执行概要信息:{}", summaryInfo);
                summaryInfoList.add(summaryInfo);
            }
        } catch (NoSuchJobException | NoSuchJobExecutionException e) {
            logger.error("{}不存在,请检查任务名是否正确!", jobName, e);
        }
        return summaryInfoList;
    }

    // TODO: 2020/12/17 加上完全放弃job和无状态关联的JOB

    @Override
    public List<BatchJobDto> getResumableJob() {
        List<BatchJobDto> batchJobDtos = new ArrayList<>(16);
        for (Map<String, Object> map : tblBatCtlService.selectJobExecution()) {
            map = new CaseInsensitiveMap<>(map);
            BatchJobDto batchJobDto = new BatchJobDto();
            batchJobDto.setJobExecutionId((Long) map.get("job_execution_id"));
            batchJobDto.setJobName((String) map.get("job_name"));
            batchJobDto.setJobInstanceId((Long) map.get("job_instance_id"));
            batchJobDto.setStatus((String) map.get("status"));
            batchJobDto.setExitCode((String) map.get("exit_code"));
            batchJobDtos.add(batchJobDto);
        }
        return batchJobDtos;
    }


    public SimpleJobLauncher getLauncher() {
        return launcher;
    }

    public void setLauncher(SimpleJobLauncher launcher) {
        this.launcher = launcher;
    }
}
