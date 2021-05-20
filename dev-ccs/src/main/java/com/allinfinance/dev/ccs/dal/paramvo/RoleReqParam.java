package com.allinfinance.dev.ccs.dal.paramvo;

import com.allinfinance.dev.ccs.dal.model.TblRole;

/**
 * @project: dev-parent
 * @description: 角色请求参数
 * @author: Lum Wang
 * @create: 2021-05-18 14:29
 */
public class RoleReqParam extends TblRole {
    private Integer current;
    private Integer pageSize;


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

    @Override
    public String toString() {
        return "RoleReqParam{" +
                "current=" + current +
                ", pageSize=" + pageSize +
                '}';
    }
}
