package com.allinfinance.dev.ccs.dal.paramvo;

/**
 * @project: dev-parent
 * @description: 权限查询请求参数
 * @author: Lum Wang
 * @create: 2021-05-18 11:19
 */
public class AuthReqParam {
    private int current;
    private int pageSize;
    private int authId;
    private String authName;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    @Override
    public String toString() {
        return "AuthReqParam{" +
                "current=" + current +
                ", pageSize=" + pageSize +
                ", authId=" + authId +
                ", authName='" + authName + '\'' +
                '}';
    }
}
