package com.allinfinance.dev.ccs.dal.paramvo;

import com.allinfinance.dev.ccs.dal.model.TblRole;

import java.util.ArrayList;
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
    private String roleId;
    private String roleName;
    private String[] roleIds;

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
    public String getRoleId() {
        return roleId;
    }

    @Override
    public void setRoleId(String roleId) {
        this.roleId = roleId;
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
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleIds=" + Arrays.toString(roleIds) +
                '}';
    }
}
