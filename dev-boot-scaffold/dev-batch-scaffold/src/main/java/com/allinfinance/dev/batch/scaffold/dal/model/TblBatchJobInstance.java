package com.allinfinance.dev.batch.scaffold.dal.model;

public class TblBatchJobInstance {
    private Long jobInstanceId;

    private Long version;

    private String jobName;

    private String jobKey;

    public TblBatchJobInstance(Long jobInstanceId, Long version, String jobName, String jobKey) {
        this.jobInstanceId = jobInstanceId;
        this.version = version;
        this.jobName = jobName;
        this.jobKey = jobKey;
    }

    public TblBatchJobInstance() {
        super();
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(Long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey == null ? null : jobKey.trim();
    }
}