package com.allinfinance.dev.batch.scaffold.dal.model;

import java.time.LocalDateTime;

public class TblBatchJobExecution {
    private Long jobExecutionId;

    private Long version;

    private Long jobInstanceId;

    private LocalDateTime createTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status;

    private String exitCode;

    private String exitMessage;

    private LocalDateTime lastUpdated;

    private String jobConfigurationLocation;

    public TblBatchJobExecution(Long jobExecutionId, Long version, Long jobInstanceId, LocalDateTime createTime, LocalDateTime startTime, LocalDateTime endTime, String status, String exitCode, String exitMessage, LocalDateTime lastUpdated, String jobConfigurationLocation) {
        this.jobExecutionId = jobExecutionId;
        this.version = version;
        this.jobInstanceId = jobInstanceId;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.exitCode = exitCode;
        this.exitMessage = exitMessage;
        this.lastUpdated = lastUpdated;
        this.jobConfigurationLocation = jobConfigurationLocation;
    }

    public TblBatchJobExecution() {
        super();
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(Long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getExitCode() {
        return exitCode;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode == null ? null : exitCode.trim();
    }

    public String getExitMessage() {
        return exitMessage;
    }

    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage == null ? null : exitMessage.trim();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getJobConfigurationLocation() {
        return jobConfigurationLocation;
    }

    public void setJobConfigurationLocation(String jobConfigurationLocation) {
        this.jobConfigurationLocation = jobConfigurationLocation == null ? null : jobConfigurationLocation.trim();
    }
}