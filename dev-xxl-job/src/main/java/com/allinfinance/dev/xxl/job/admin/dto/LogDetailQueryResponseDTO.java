package com.allinfinance.dev.xxl.job.admin.dto;

import java.util.Date;

/**
 * @author huanghf
 * @date 2021/12/31 17:02
 */
public class LogDetailQueryResponseDTO {
    private int triggerCode;
    private int handleCode;
    private String executorAddress;
    private Date triggerTime;
    private long logId;

    public int getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(int triggerCode) {
        this.triggerCode = triggerCode;
    }

    public int getHandleCode() {
        return handleCode;
    }

    public void setHandleCode(int handleCode) {
        this.handleCode = handleCode;
    }

    public String getExecutorAddress() {
        return executorAddress;
    }

    public void setExecutorAddress(String executorAddress) {
        this.executorAddress = executorAddress;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    @Override
    public String toString() {
        return "LogDetailQueryResponseDTO{" +
                "triggerCode=" + triggerCode +
                ", handleCode=" + handleCode +
                ", executorAddress='" + executorAddress + '\'' +
                ", triggerTime=" + triggerTime +
                ", logId='" + logId + '\'' +
                '}';
    }
}
