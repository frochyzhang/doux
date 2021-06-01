package com.allinfinance.dev.ccs.dal.paramvo;

import com.allinfinance.dev.ccs.dal.model.TblAuth;

import java.util.Arrays;

/**
 * @project: dev-parent
 * @description: 权限查询请求参数
 * @author: Lum Wang
 * @create: 2021-05-18 11:19
 */
public class AuthReqParam extends TblAuth {
    private Integer current;
    private Integer pageSize;
    private String[] authIds;


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


    public String[] getAuthIds() {
        return authIds;
    }

    public void setAuthIds(String[] authIds) {
        this.authIds = authIds;
    }

    @Override
    public String toString() {
        return "AuthReqParam{" +
                "current=" + current +
                ", pageSize=" + pageSize +
                ", authIds=" + Arrays.toString(authIds) +
                '}';
    }
}
