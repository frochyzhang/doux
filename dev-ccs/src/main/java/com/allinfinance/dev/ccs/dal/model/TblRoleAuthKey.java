package com.allinfinance.dev.ccs.dal.model;

import java.io.Serializable;

public class TblRoleAuthKey implements Serializable {
    private String roleId;

    private String authId;

    private static final long serialVersionUID = 1L;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }
}