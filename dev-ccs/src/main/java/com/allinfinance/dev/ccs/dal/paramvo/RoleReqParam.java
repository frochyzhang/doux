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

    private String roleName;
    private String org;
    private String isAvailable;
    private String weight;
    private String roleId;
    private String[] roleIds;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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
                "roleName='" + roleName + '\'' +
                ", org='" + org + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", weight='" + weight + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleIds=" + Arrays.toString(roleIds) +
                '}';
    }
}
