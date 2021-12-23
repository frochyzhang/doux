package com.allinfinance.dev.ccs.dal.model;

public class TblRoleAuthKey {
    private String roleId;

    private String authId;

    public TblRoleAuthKey(String roleId, String authId) {
        this.roleId = roleId;
        this.authId = authId;
    }

    public TblRoleAuthKey() {
        super();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId == null ? null : authId.trim();
    }
}