package com.allinfinance.dev.ccs.dal.model;

import java.io.Serializable;

public class TblRolePermissionInfo implements Serializable {
    private String id;

    private String roleId;

    private String permissioncode;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissioncode() {
        return permissioncode;
    }

    public void setPermissioncode(String permissioncode) {
        this.permissioncode = permissioncode;
    }

    @Override
    public String toString() {
        return "TblRolePermissionInfo{" +
                "id='" + id + '\'' +
                ", roleId='" + roleId + '\'' +
                ", permissioncode='" + permissioncode + '\'' +
                '}';
    }
}