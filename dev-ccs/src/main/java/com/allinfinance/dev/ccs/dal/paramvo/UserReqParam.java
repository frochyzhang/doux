package com.allinfinance.dev.ccs.dal.paramvo;

import java.util.Arrays;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/5/14 10:16
 * @description：用户页面请求参数
 */

public class UserReqParam extends BaseReqParam {

    private String userName;
    private String roleId;
    private String org;
    private String confirmpass;
    private String userPass;
    private String mobileNo;
    private String isAvailable;

    public String getConfirmpass() {
        return confirmpass;
    }

    public void setConfirmpass(String confirmpass) {
        this.confirmpass = confirmpass;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    private String[] userIds;

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    @Override
    public String toString() {
        return "UserReqParam{" +
                "userName='" + userName + '\'' +
                ", roleId='" + roleId + '\'' +
                ", org='" + org + '\'' +
                ", confirmpass='" + confirmpass + '\'' +
                ", userPass='" + userPass + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", userIds=" + Arrays.toString(userIds) +
                '}';
    }
}
