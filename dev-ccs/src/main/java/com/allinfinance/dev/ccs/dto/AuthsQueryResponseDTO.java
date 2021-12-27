package com.allinfinance.dev.ccs.dto;

/**
 * @author huanghf
 * @date 2021/12/24 15:33
 */
public class AuthsQueryResponseDTO {
    private String authId;
    private String authName;
    private String org;
    private String isAvailable;

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

    @Override
    public String toString() {
        return "AuthsQueryResponseDTO{" +
                "authId='" + authId + '\'' +
                ", authName='" + authName + '\'' +
                ", org='" + org + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                '}';
    }
}
