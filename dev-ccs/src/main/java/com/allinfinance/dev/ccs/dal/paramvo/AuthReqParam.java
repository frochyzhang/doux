package com.allinfinance.dev.ccs.dal.paramvo;

import java.util.Arrays;

/**
 * @project: dev-parent
 * @description: 权限查询请求参数
 * @author: Lum Wang
 * @create: 2021-05-18 11:19
 */
public class AuthReqParam extends BaseReqParam {
    private String authName;
    private String org;
    private String isAvailable;
    private String[] authIds;

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
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

    public String[] getAuthIds() {
        return authIds;
    }

    public void setAuthIds(String[] authIds) {
        this.authIds = authIds;
    }

    @Override
    public String toString() {
        return "AuthReqParam{" +
                "authName='" + authName + '\'' +
                ", org='" + org + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", authIds=" + Arrays.toString(authIds) +
                '}';
    }
}
