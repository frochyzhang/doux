package com.allinfinance.dev.ccs.dto;

import java.util.List;

/**
 * @author huanghf
 * @date 2021/12/24 10:19
 */
public class RoleInfoUpdateRequestDTO {
    private String roleId;
    private String roleName;
    private String isAvailable;
    private String org;
    private String weight;
    private String auth;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "RoleInfoUpdateRequestDTO{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", org='" + org + '\'' +
                ", weight='" + weight + '\'' +
                ", auth=" + auth +
                '}';
    }
}
