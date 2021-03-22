package com.allinfinance.dev.batch.basic;

import com.allinfinance.dev.core.bean.BatchJobDto;
import com.allinfinance.dev.core.dto.JobSummaryInfo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IBasicBatchService
 *
 * @author hongmr
 * @date 2017/1/6
 */
public interface IBasicBatchService {
    /**
     * 启动SpringBatch Job
     *
     * @param job       任务
     * @param jobParams 任务参数
     * @return 任务执行结果
     */
    public JobExecution startNewBatch(Job job, HashMap<String, String> jobParams);

    /**
     * Start a new instance of a job with the parameters specified.
     *
     * @param jobName    the name of the {@link Job} to launch
     * @param parameters the parameters to launch it with (comma or newline
     *                   separated name=value pairs)
     * @return the id of the {@link JobExecution} that is launched
     * @throws NoSuchJobException                if there is no {@link Job} with the specified
     *                                           name
     * @throws JobInstanceAlreadyExistsException if a job instance with this
     *                                           name and parameters already exists
     * @throws JobParametersInvalidException     thrown if any of the job parameters are invalid.
     */
    Long startJob(String jobName, String parameters) throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException;

    /**
     * 暂停SpringBatch Job
     */
    Map<Long, Boolean> pauseJob(List<Long> executionIdList);

    /**
     * Restart a failed or stopped {@link JobExecution}. Fails with an exception
     * if the id provided does not exist or corresponds to a {@link JobInstance}
     * that in normal circumstances already completed successfully.
     *
     * @param jobExecutionId the id of a failed or stopped {@link JobExecution}
     * @return the id of the {@link JobExecution} that was started
     * @throws JobInstanceAlreadyCompleteException if the job was already
     *                                             successfully completed
     * @throws NoSuchJobExecutionException         if the id was not associated with any
     *                                             {@link JobExecution}
     * @throws NoSuchJobException                  if the {@link JobExecution} was found, but its
     *                                             corresponding {@link Job} is no longer available for launching
     * @throws JobRestartException                 if there is a non-specific error with the
     *                                             restart (e.g. corrupt or inconsistent restart data)
     * @throws JobParametersInvalidException       if the parameters are not valid for
     *                                             this job
     */
    Long resumeJob(Long jobExecutionId) throws JobInstanceAlreadyCompleteException;

    /**
     * 根据任务名暂停任务
     *
     * @param jobName
     * @return
     * @throws JobInstanceAlreadyCompleteException
     */
    List<Long> resumeJob(String jobName) throws JobInstanceAlreadyCompleteException;

    /**
     * 批量暂停任务
     *
     * @param jobExecutionIds
     * @return
     * @throws JobInstanceAlreadyCompleteException
     */
    List<Long> resumeJob(List<Long> jobExecutionIds) throws JobInstanceAlreadyCompleteException;

    List<Long> abandonJob(List<Long> jobExecutionIds);

    List<JobSummaryInfo> getJobSummaryInfo(List<Long> jobExecutionIdList);

    List<BatchJobDto> getResumableJob();

    List<Long> getRunningExecution(String jobName);

    String getParameters(Long jobExecutionId);

}
