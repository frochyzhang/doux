package com.allinfinance.dev.ccs.dal.paramvo;

import com.allinfinance.dev.ccs.dal.model.TblRole;

import java.util.Arrays;

/**
 * @project: dev-parent
 * @description: 角色请求参数
 * @author: Lum Wang
 * @create: 2021-05-18 14:29
 */
public class RoleReqParam extends BaseReqParam {

    private String userId;
    private String roleName;
    private String isAvailable;
    private String org;
    private String roleId;
    private String[] roleIds;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "RoleReqParam{" +
                "userId='" + userId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", org='" + org + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleIds=" + Arrays.toString(roleIds) +
                '}';
    }
}
