package com.allinfinance.dev.ccs.dal.model;

import java.util.Date;

public class TblUserErrorLog {
    private Integer excId;

    private String excRequParam;

    private String excName;

    private String excMessage;

    private String operUserId;

    private String operUserName;

    private String operMethod;

    private String operUri;

    private String operIp;

    private Date operCreateTime;

    private String reservedField1;

    private String reservedField2;

    private String reservedField3;

    private String org;

    public TblUserErrorLog(Integer excId, String excRequParam, String excName, String excMessage, String operUserId, String operUserName, String operMethod, String operUri, String operIp, Date operCreateTime, String reservedField1, String reservedField2, String reservedField3, String org) {
        this.excId = excId;
        this.excRequParam = excRequParam;
        this.excName = excName;
        this.excMessage = excMessage;
        this.operUserId = operUserId;
        this.operUserName = operUserName;
        this.operMethod = operMethod;
        this.operUri = operUri;
        this.operIp = operIp;
        this.operCreateTime = operCreateTime;
        this.reservedField1 = reservedField1;
        this.reservedField2 = reservedField2;
        this.reservedField3 = reservedField3;
        this.org = org;
    }

    public TblUserErrorLog() {
        super();
    }

    public Integer getExcId() {
        return excId;
    }

    public void setExcId(Integer excId) {
        this.excId = excId;
    }

    public String getExcRequParam() {
        return excRequParam;
    }

    public void setExcRequParam(String excRequParam) {
        this.excRequParam = excRequParam == null ? null : excRequParam.trim();
    }

    public String getExcName() {
        return excName;
    }

    public void setExcName(String excName) {
        this.excName = excName == null ? null : excName.trim();
    }

    public String getExcMessage() {
        return excMessage;
    }

    public void setExcMessage(String excMessage) {
        this.excMessage = excMessage == null ? null : excMessage.trim();
    }

    public String getOperUserId() {
        return operUserId;
    }

    public void setOperUserId(String operUserId) {
        this.operUserId = operUserId == null ? null : operUserId.trim();
    }

    public String getOperUserName() {
        return operUserName;
    }

    public void setOperUserName(String operUserName) {
        this.operUserName = operUserName == null ? null : operUserName.trim();
    }

    public String getOperMethod() {
        return operMethod;
    }

    public void setOperMethod(String operMethod) {
        this.operMethod = operMethod == null ? null : operMethod.trim();
    }

    public String getOperUri() {
        return operUri;
    }

    public void setOperUri(String operUri) {
        this.operUri = operUri == null ? null : operUri.trim();
    }

    public String getOperIp() {
        return operIp;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp == null ? null : operIp.trim();
    }

    public Date getOperCreateTime() {
        return operCreateTime;
    }

    public void setOperCreateTime(Date operCreateTime) {
        this.operCreateTime = operCreateTime;
    }

    public String getReservedField1() {
        return reservedField1;
    }

    public void setReservedField1(String reservedField1) {
        this.reservedField1 = reservedField1 == null ? null : reservedField1.trim();
    }

    public String getReservedField2() {
        return reservedField2;
    }

    public void setReservedField2(String reservedField2) {
        this.reservedField2 = reservedField2 == null ? null : reservedField2.trim();
    }

    public String getReservedField3() {
        return reservedField3;
    }

    public void setReservedField3(String reservedField3) {
        this.reservedField3 = reservedField3 == null ? null : reservedField3.trim();
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org == null ? null : org.trim();
    }
}