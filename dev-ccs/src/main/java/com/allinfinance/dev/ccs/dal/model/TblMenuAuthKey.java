package com.allinfinance.dev.ccs.dal.model;

import java.io.Serializable;

public class TblMenuAuthKey implements Serializable {
    private String authId;

    private String menuId;

    private static final long serialVersionUID = 1L;

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}