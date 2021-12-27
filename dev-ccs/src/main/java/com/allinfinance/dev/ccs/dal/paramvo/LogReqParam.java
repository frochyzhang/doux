package com.allinfinance.dev.ccs.dal.paramvo;


import java.util.Date;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/5/17 10:56
 * @description：操作日志请求参数
 */
public class LogReqParam extends BaseReqParam {

    private String time;
    private String org;
    private String operUserName;
    private String operType;
    private String operDesc;
    private Date beginDate;
    private Date endDate;


    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getOperUserName() {
        return operUserName;
    }

    public void setOperUserName(String operUserName) {
        this.operUserName = operUserName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getOperDesc() {
        return operDesc;
    }

    public void setOperDesc(String operDesc) {
        this.operDesc = operDesc;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "LogReqParam{" +
                "time='" + time + '\'' +
                ", org='" + org + '\'' +
                ", operUserName='" + operUserName + '\'' +
                ", operType='" + operType + '\'' +
                ", operDesc='" + operDesc + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
