package com.allinfinance.dev.ccs.dal.paramvo;

import com.allinfinance.dev.ccs.dal.model.TblRole;

import java.util.Arrays;

/**
 * @project: dev-parent
 * @description: 角色请求参数
 * @author: Lum Wang
 * @create: 2021-05-18 14:29
 */
public class RoleReqParam extends TblRole {
    private Integer current;
    private Integer pageSize;
    private String userId;
    private String roleName;
    private String[] roleIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

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
    public String getRoleName() {
        return roleName;
    }

    @Override
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleReqParam{" +
                "current=" + current +
                ", pageSize=" + pageSize +
                ", roleName='" + roleName + '\'' +
                ", roleIds=" + Arrays.toString(roleIds) +
                '}';
    }
}
