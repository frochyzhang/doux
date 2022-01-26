package com.allinfinance.dev.batch.scaffold.dal.model;

import java.time.LocalDateTime;

public class TblBatchJobExecutionParams {
    private Long jobExecutionId;

    private String typeCd;

    private String keyName;

    private String stringVal;

    private LocalDateTime dateVal;

    private Long longVal;

    private Double doubleVal;

    private String identifying;

    public TblBatchJobExecutionParams(Long jobExecutionId, String typeCd, String keyName, String stringVal, LocalDateTime dateVal, Long longVal, Double doubleVal, String identifying) {
        this.jobExecutionId = jobExecutionId;
        this.typeCd = typeCd;
        this.keyName = keyName;
        this.stringVal = stringVal;
        this.dateVal = dateVal;
        this.longVal = longVal;
        this.doubleVal = doubleVal;
        this.identifying = identifying;
    }

    public TblBatchJobExecutionParams() {
        super();
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd == null ? null : typeCd.trim();
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName == null ? null : keyName.trim();
    }

    public String getStringVal() {
        return stringVal;
    }

    public void setStringVal(String stringVal) {
        this.stringVal = stringVal == null ? null : stringVal.trim();
    }

    public LocalDateTime getDateVal() {
        return dateVal;
    }

    public void setDateVal(LocalDateTime dateVal) {
        this.dateVal = dateVal;
    }

    public Long getLongVal() {
        return longVal;
    }

    public void setLongVal(Long longVal) {
        this.longVal = longVal;
    }

    public Double getDoubleVal() {
        return doubleVal;
    }

    public void setDoubleVal(Double doubleVal) {
        this.doubleVal = doubleVal;
    }

    public String getIdentifying() {
        return identifying;
    }

    public void setIdentifying(String identifying) {
        this.identifying = identifying == null ? null : identifying.trim();
    }
}