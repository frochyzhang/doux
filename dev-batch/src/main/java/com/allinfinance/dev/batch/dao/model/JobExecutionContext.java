package com.allinfinance.dev.batch.dao.model;

import java.io.Serializable;

/**
 * batch_job_execution_context
 *
 * @author
 */
public class JobExecutionContext implements Serializable {
    private Long jobExecutionId;

    private String shortContext;

    private String serializedContext;

    private static final long serialVersionUID = 1L;

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public String getShortContext() {
        return shortContext;
    }

    public void setShortContext(String shortContext) {
        this.shortContext = shortContext;
    }

    public String getSerializedContext() {
        return serializedContext;
    }

    public void setSerializedContext(String serializedContext) {
        this.serializedContext = serializedContext;
    }
}