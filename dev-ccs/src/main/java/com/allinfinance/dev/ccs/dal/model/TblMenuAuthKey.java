package com.allinfinance.dev.ccs.dal.model;

public class TblMenuAuthKey {
    private String authId;

    private String menuId;

    public TblMenuAuthKey(String authId, String menuId) {
        this.authId = authId;
        this.menuId = menuId;
    }

    public TblMenuAuthKey() {
        super();
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId == null ? null : authId.trim();
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
    }
}