package com.allinfinance.dev.ccs.dto;


/**
 * @author huanghf
 * @date 2021/12/24 10:57
 */
public class RoleCreateRequestDTO {
    private String roleName;
    private String isAvailable;
    private String org;
    private String weight;
    private String auth;

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
        return "RoleCreateRequestDTO{" +
                "roleName='" + roleName + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", org='" + org + '\'' +
                ", weight='" + weight + '\'' +
                ", auth=" + auth +
                '}';
    }
}
