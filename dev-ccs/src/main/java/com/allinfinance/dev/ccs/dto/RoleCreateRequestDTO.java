package com.allinfinance.dev.ccs.dto;

import java.util.List;

/**
 * @author huanghf
 * @date 2021/12/24 10:57
 */
public class RoleCreateRequestDTO {
    private String roleName;
    private String isAvailable;
    private String org;
    private String weight;
    private List<String> auth;

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

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "RoleCreateRequestDTO{" +
                "roleName='" + roleName + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", org='" + org + '\'' +
                ", weight='" + weight + '\'' +
                ", auth=" + auth +
                '}';
    }
}
