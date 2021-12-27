package com.allinfinance.dev.ccs.dto;

import java.util.List;

/**
 * @author huanghf
 * @date 2021/12/24 15:54
 */
public class AuthInfoUpdateRequestDTO {
    private String authId;
    private String authName;
    private String isAvailable;
    private String org;
    private List<String> menus;

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
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

    public List<String> getMenus() {
        return menus;
    }

    public void setMenus(List<String> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "AuthInfoUpdateRequestDTO{" +
                "authId='" + authId + '\'' +
                ", authName='" + authName + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", org='" + org + '\'' +
                ", menus=" + menus +
                '}';
    }
}
