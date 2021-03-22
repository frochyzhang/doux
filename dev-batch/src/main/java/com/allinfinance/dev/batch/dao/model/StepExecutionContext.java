package com.allinfinance.dev.batch.dao.model;

import java.io.Serializable;

/**
 * batch_step_execution_context
 * @author 
 */
public class StepExecutionContext implements Serializable {
    private Long stepExecutionId;

    private String shortContext;

    private String serializedContext;

    private static final long serialVersionUID = 1L;

    public Long getStepExecutionId() {
        return stepExecutionId;
    }

    public void setStepExecutionId(Long stepExecutionId) {
        this.stepExecutionId = stepExecutionId;
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