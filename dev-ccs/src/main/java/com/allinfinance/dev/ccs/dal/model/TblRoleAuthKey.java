package com.allinfinance.dev.ccs.dal.model;

import java.io.Serializable;

public class TblRoleAuthKey implements Serializable {
    private String authId;

    private String  roleId;

    private static final long serialVersionUID = 1L;

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}