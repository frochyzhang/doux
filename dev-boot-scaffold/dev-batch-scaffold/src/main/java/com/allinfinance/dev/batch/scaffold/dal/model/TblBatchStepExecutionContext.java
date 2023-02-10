package com.allinfinance.dev.batch.scaffold.dal.model;

public class TblBatchStepExecutionContext {
    private Long stepExecutionId;

    private String shortContext;

    private byte[] serializedContext;

    public TblBatchStepExecutionContext(Long stepExecutionId, String shortContext) {
        this.stepExecutionId = stepExecutionId;
        this.shortContext = shortContext;
    }

    public TblBatchStepExecutionContext(Long stepExecutionId, String shortContext, byte[] serializedContext) {
        this.stepExecutionId = stepExecutionId;
        this.shortContext = shortContext;
        this.serializedContext = serializedContext;
    }

    public TblBatchStepExecutionContext() {
        super();
    }

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
        this.shortContext = shortContext == null ? null : shortContext.trim();
    }

    public byte[] getSerializedContext() {
        return serializedContext;
    }

    public void setSerializedContext(byte[] serializedContext) {
        this.serializedContext = serializedContext;
    }
}
