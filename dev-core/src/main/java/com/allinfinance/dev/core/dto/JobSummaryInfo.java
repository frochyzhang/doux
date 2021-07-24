package com.allinfinance.dev.core.dto;

import java.util.Date;
import java.util.List;

/**
 * @author 张勇
 * @description
 * @date 2020/12/24 10:38
 */
public class JobSummaryInfo {

    //job execution
    private Long jobExecutionId;
    private Long jobExecutionVersion;
    private Date startTime;
    private Date endTime;
    private Date lastUpdated;
    private String status;
    private String exitCode;
    private String exitMessage;

    //job instance
    private Long jobInstanceId;
    private Long jobInstanceVersion;
    private String jobName;

    //job params
    private List<JobParamsDto> jobParamsDtoList;

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public Long getJobExecutionVersion() {
        return jobExecutionVersion;
    }

    public void setJobExecutionVersion(Long jobExecutionVersion) {
        this.jobExecutionVersion = jobExecutionVersion;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public String getExitMessage() {
        return exitMessage;
    }

    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(Long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public Long getJobInstanceVersion() {
        return jobInstanceVersion;
    }

    public void setJobInstanceVersion(Long jobInstanceVersion) {
        this.jobInstanceVersion = jobInstanceVersion;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public List<JobParamsDto> getJobParamsDtoList() {
        return jobParamsDtoList;
    }

    public void setJobParamsDtoList(List<JobParamsDto> jobParamsDtoList) {
        this.jobParamsDtoList = jobParamsDtoList;
    }

    @Override
    public String toString() {
        return "JobSummaryInfo{" +
                "jobExecutionId=" + jobExecutionId +
                ", jobExecutionVersion=" + jobExecutionVersion +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lastUpdated=" + lastUpdated +
                ", status='" + status + '\'' +
                ", exitCode='" + exitCode + '\'' +
                ", exitMessage='" + exitMessage + '\'' +
                ", jobInstanceId=" + jobInstanceId +
                ", jobInstanceVersion=" + jobInstanceVersion +
                ", jobName='" + jobName + '\'' +
                ", jobParamsDtoList=' " + jobParamsDtoList + '\'' +
                '}';
    }
}
