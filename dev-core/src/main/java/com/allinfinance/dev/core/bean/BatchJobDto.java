package com.allinfinance.dev.core.bean;

/**
 * @author 张勇
 * @description
 * @date 2020/12/9 20:12
 */
public class BatchJobDto {
    private String jobName;
    private Long jobExecutionId;
    private Long jobInstanceId;
    private String status;
    private String exitCode;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(Long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExitCode() {
        return exitCode;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    @Override
    public String toString() {
        return "BatchJobDto{" +
                "jobName='" + jobName + '\'' +
                ", jobExecutionId=" + jobExecutionId +
                ", jobInstanceId=" + jobInstanceId +
                ", status='" + status + '\'' +
                ", exitCode='" + exitCode + '\'' +
                '}';
    }
}
