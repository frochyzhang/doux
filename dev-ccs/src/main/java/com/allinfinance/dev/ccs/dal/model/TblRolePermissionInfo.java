package com.allinfinance.dev.ccs.dal.model;

public class TblRolePermissionInfo {
    private String id;

    private String roleId;

    private String permissioncode;

    public TblRolePermissionInfo(String id, String roleId, String permissioncode) {
        this.id = id;
        this.roleId = roleId;
        this.permissioncode = permissioncode;
    }

    public TblRolePermissionInfo() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getPermissioncode() {
        return permissioncode;
    }

    public void setPermissioncode(String permissioncode) {
        this.permissioncode = permissioncode == null ? null : permissioncode.trim();
    }
}