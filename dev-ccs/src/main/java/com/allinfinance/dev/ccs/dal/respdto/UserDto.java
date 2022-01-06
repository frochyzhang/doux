package com.allinfinance.dev.ccs.dal.respdto;

import java.util.Date;

/**
 * @author ：lqf
 * @project :dev-parent
 * @date ：2022/1/6 17:51
 * @description：返回的currentUser
 */
public class UserDto {

    private String userId;

    private String userName;

    private String userPass;

    private String roleId;

    private String org;

    private String orgName;

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getInitPass() {
        return initPass;
    }

    public void setInitPass(String initPass) {
        this.initPass = initPass;
    }

    public String getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(String errorNum) {
        this.errorNum = errorNum;
    }

    public String getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(String passStatus) {
        this.passStatus = passStatus;
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
        this.createBy = createBy;
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
        this.updateBy = updateBy;
    }

    public String getReservedField1() {
        return reservedField1;
    }

    public void setReservedField1(String reservedField1) {
        this.reservedField1 = reservedField1;
    }

    public String getReservedField2() {
        return reservedField2;
    }

    public void setReservedField2(String reservedField2) {
        this.reservedField2 = reservedField2;
    }

    public String getReservedField3() {
        return reservedField3;
    }

    public void setReservedField3(String reservedField3) {
        this.reservedField3 = reservedField3;
    }

    public String getReservedField4() {
        return reservedField4;
    }

    public void setReservedField4(String reservedField4) {
        this.reservedField4 = reservedField4;
    }

    public String getReservedField5() {
        return reservedField5;
    }

    public void setReservedField5(String reservedField5) {
        this.reservedField5 = reservedField5;
    }

    public String getReservedField6() {
        return reservedField6;
    }

    public void setReservedField6(String reservedField6) {
        this.reservedField6 = reservedField6;
    }

    public String getReservedField7() {
        return reservedField7;
    }

    public void setReservedField7(String reservedField7) {
        this.reservedField7 = reservedField7;
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
        this.notExpired = notExpired;
    }

    public String getAccountNotLocked() {
        return accountNotLocked;
    }

    public void setAccountNotLocked(String accountNotLocked) {
        this.accountNotLocked = accountNotLocked;
    }

    public String getCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    public void setCredentialsNotExpired(String credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", roleId='" + roleId + '\'' +
                ", org='" + org + '\'' +
                ", orgName='" + orgName + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", initPass='" + initPass + '\'' +
                ", errorNum='" + errorNum + '\'' +
                ", passStatus='" + passStatus + '\'' +
                ", lastPassUpdateTime=" + lastPassUpdateTime +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", updateBy='" + updateBy + '\'' +
                ", reservedField1='" + reservedField1 + '\'' +
                ", reservedField2='" + reservedField2 + '\'' +
                ", reservedField3='" + reservedField3 + '\'' +
                ", reservedField4='" + reservedField4 + '\'' +
                ", reservedField5='" + reservedField5 + '\'' +
                ", reservedField6='" + reservedField6 + '\'' +
                ", reservedField7='" + reservedField7 + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", notExpired='" + notExpired + '\'' +
                ", accountNotLocked='" + accountNotLocked + '\'' +
                ", credentialsNotExpired='" + credentialsNotExpired + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}
