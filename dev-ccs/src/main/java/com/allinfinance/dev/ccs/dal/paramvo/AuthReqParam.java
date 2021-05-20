package com.allinfinance.dev.ccs.dal.paramvo;

/**
 * @project: dev-parent
 * @description: 权限查询请求参数
 * @author: Lum Wang
 * @create: 2021-05-18 11:19
 */
public class AuthReqParam {
    private Integer current;
    private Integer pageSize;
    private String authId;
    private String authName;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
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
                ", authId='" + authId + '\'' +
                ", authName='" + authName + '\'' +
                '}';
    }
}
