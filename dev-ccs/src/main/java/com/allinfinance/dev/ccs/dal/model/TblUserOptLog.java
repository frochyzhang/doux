package com.allinfinance.dev.ccs.dal.model;

import java.util.Date;

public class TblUserOptLog {
    private Integer operId;

    private String operModule;

    private String operType;

    private String operDesc;

    private String operRequParam;

    private String operRespParam;

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

    public TblUserOptLog(Integer operId, String operModule, String operType, String operDesc, String operRequParam, String operRespParam, String operUserId, String operUserName, String operMethod, String operUri, String operIp, Date operCreateTime, String reservedField1, String reservedField2, String reservedField3, String org) {
        this.operId = operId;
        this.operModule = operModule;
        this.operType = operType;
        this.operDesc = operDesc;
        this.operRequParam = operRequParam;
        this.operRespParam = operRespParam;
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

    public TblUserOptLog() {
        super();
    }

    public Integer getOperId() {
        return operId;
    }

    public void setOperId(Integer operId) {
        this.operId = operId;
    }

    public String getOperModule() {
        return operModule;
    }

    public void setOperModule(String operModule) {
        this.operModule = operModule == null ? null : operModule.trim();
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType == null ? null : operType.trim();
    }

    public String getOperDesc() {
        return operDesc;
    }

    public void setOperDesc(String operDesc) {
        this.operDesc = operDesc == null ? null : operDesc.trim();
    }

    public String getOperRequParam() {
        return operRequParam;
    }

    public void setOperRequParam(String operRequParam) {
        this.operRequParam = operRequParam == null ? null : operRequParam.trim();
    }

    public String getOperRespParam() {
        return operRespParam;
    }

    public void setOperRespParam(String operRespParam) {
        this.operRespParam = operRespParam == null ? null : operRespParam.trim();
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