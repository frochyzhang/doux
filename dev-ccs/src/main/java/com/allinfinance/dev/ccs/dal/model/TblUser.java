package com.allinfinance.dev.ccs.dal.model;

import java.io.Serializable;
import java.util.Date;

public class TblUser implements Serializable {
    private String userId;

    private String userName;

    private String userPass;

    private String roleId;

    private String org;

    private String branchId;

    private String bankId;

    private String merchant;

    private String isAvailable;

    private String initPass;

    private String errorNum;

    private String passStatus;

    private Date lastPassUpdateTime;

    private String salt;

    private String createBy;

    private Date createTime;

    private Date updateTime;

    private String updateBy;

    private String reservedField1;

    private String reservedField2;

    private String reservedField3;

    private Date lastLoginTime;

    private String notExpired;

    private String accountNotLocked;

    private String credentialsNotExpired;

    private static final long serialVersionUID = 1L;

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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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
}