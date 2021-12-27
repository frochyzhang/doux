package com.allinfinance.dev.ccs.dto;

import java.util.List;

/**
 * @author huanghf
 * @date 2021/12/23 14:12
 */
public class PageableRolesQueryResponseDTO {
    private String roleId;

    private String roleName;

    private String isAvailable;

    private String weight;

    private String org;

    private List<String> auth;

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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "PageableRolesQueryResponseDTO{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", weight='" + weight + '\'' +
                ", org='" + org + '\'' +
                ", auth=" + auth +
                '}';
    }
}
