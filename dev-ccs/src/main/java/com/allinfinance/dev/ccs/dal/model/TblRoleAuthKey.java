package com.allinfinance.dev.ccs.dal.model;

import java.io.Serializable;

public class TblRoleAuthKey implements Serializable {
    private Integer authId;

    private Integer roleId;

    private static final long serialVersionUID = 1L;

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}