package com.allinfinance.dev.ccs.dal.model;

import java.util.Date;

public class TblUser {
    private String userId;

    private String userName;

    private String userPass;

    private String roleId;

    private String org;

    private String isAvailable;

    private String initPass;

    private String errorNum;

    private String passStatus;

    private Date lastPassUpdateTime;

    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String updateBy;

    private String reservedField1;

    private String reservedField2;

    private String reservedField3;

    private String reservedField4;

    private String reservedField5;

    private String reservedField6;

    private String reservedField7;

    private Date lastLoginTime;

    private String notExpired;

    private String accountNotLocked;

    private String credentialsNotExpired;

    private String mobileNo;

    private String otpFlag;

    public TblUser(String userId, String userName, String userPass, String roleId, String org, String isAvailable, String initPass, String errorNum, String passStatus, Date lastPassUpdateTime, String createBy, Date createTime, Date updateTime, String updateBy, String reservedField1, String reservedField2, String reservedField3, String reservedField4, String reservedField5, String reservedField6, String reservedField7, Date lastLoginTime, String notExpired, String accountNotLocked, String credentialsNotExpired, String mobileNo, String otpFlag) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
        this.roleId = roleId;
        this.org = org;
        this.isAvailable = isAvailable;
        this.initPass = initPass;
        this.errorNum = errorNum;
        this.passStatus = passStatus;
        this.lastPassUpdateTime = lastPassUpdateTime;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
        this.reservedField1 = reservedField1;
        this.reservedField2 = reservedField2;
        this.reservedField3 = reservedField3;
        this.reservedField4 = reservedField4;
        this.reservedField5 = reservedField5;
        this.reservedField6 = reservedField6;
        this.reservedField7 = reservedField7;
        this.lastLoginTime = lastLoginTime;
        this.notExpired = notExpired;
        this.accountNotLocked = accountNotLocked;
        this.credentialsNotExpired = credentialsNotExpired;
        this.mobileNo = mobileNo;
        this.otpFlag = otpFlag;
    }

    public TblUser() {
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass == null ? null : userPass.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org == null ? null : org.trim();
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable == null ? null : isAvailable.trim();
    }

    public String getInitPass() {
        return initPass;
    }

    public void setInitPass(String initPass) {
        this.initPass = initPass == null ? null : initPass.trim();
    }

    public String getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(String errorNum) {
        this.errorNum = errorNum == null ? null : errorNum.trim();
    }

    public String getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(String passStatus) {
        this.passStatus = passStatus == null ? null : passStatus.trim();
    }

    public Date getLastPassUpdateTime() {
        return lastPassUpdateTime;
    }

    public void setLastPassUpdateTime(Date lastPassUpdateTime) {
        this.lastPassUpdateTime = lastPassUpdateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
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

    public String getReservedField4() {
        return reservedField4;
    }

    public void setReservedField4(String reservedField4) {
        this.reservedField4 = reservedField4 == null ? null : reservedField4.trim();
    }

    public String getReservedField5() {
        return reservedField5;
    }

    public void setReservedField5(String reservedField5) {
        this.reservedField5 = reservedField5 == null ? null : reservedField5.trim();
    }

    public String getReservedField6() {
        return reservedField6;
    }

    public void setReservedField6(String reservedField6) {
        this.reservedField6 = reservedField6 == null ? null : reservedField6.trim();
    }

    public String getReservedField7() {
        return reservedField7;
    }

    public void setReservedField7(String reservedField7) {
        this.reservedField7 = reservedField7 == null ? null : reservedField7.trim();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getNotExpired() {
        return notExpired;
    }

    public void setNotExpired(String notExpired) {
        this.notExpired = notExpired == null ? null : notExpired.trim();
    }

    public String getAccountNotLocked() {
        return accountNotLocked;
    }

    public void setAccountNotLocked(String accountNotLocked) {
        this.accountNotLocked = accountNotLocked == null ? null : accountNotLocked.trim();
    }

    public String getCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    public void setCredentialsNotExpired(String credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired == null ? null : credentialsNotExpired.trim();
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getOtpFlag() {
        return otpFlag;
    }

    public void setOtpFlag(String otpFlag) {
        this.otpFlag = otpFlag == null ? null : otpFlag.trim();
    }
}