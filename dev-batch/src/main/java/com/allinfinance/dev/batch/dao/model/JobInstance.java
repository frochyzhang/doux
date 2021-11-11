package com.allinfinance.dev.batch.dao.model;

import java.io.Serializable;

/**
 * batch_job_instance
 *
 * @author
 */
public class JobInstance implements Serializable {
    private Long jobInstanceId;

    private Long version;

    private String jobName;

    private String jobKey;

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
        this.jobName = jobName;
    }

    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }

    @Override
    public String toString() {
        return "JobInstance{" +
                "jobInstanceId=" + jobInstanceId +
                ", version=" + version +
                ", jobName='" + jobName + '\'' +
                ", jobKey='" + jobKey + '\'' +
                '}';
    }
}