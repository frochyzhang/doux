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

    private String mobileNo;

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
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
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}
