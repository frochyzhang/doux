package com.allinfinance.dev.ccs.dal.paramvo;

/**
 * @project: dev-parent
 * @description: 角色请求参数
 * @author: Lum Wang
 * @create: 2021-05-18 14:29
 */
public class RoleReqParam {
    private int current;
    private int pageSize;
    private int roleId;
    private String roleName;

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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleReqParam{" +
                "current=" + current +
                ", pageSize=" + pageSize +
                ", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
