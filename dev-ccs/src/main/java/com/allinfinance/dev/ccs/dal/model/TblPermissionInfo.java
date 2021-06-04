package com.allinfinance.dev.ccs.dal.model;

import java.io.Serializable;

public class TblPermissionInfo implements Serializable {
    private String permissioncode;

    private String url;

    private String description;

    private String enable;

    private String field1;

    private static final long serialVersionUID = 1L;

    public String getPermissioncode() {
        return permissioncode;
    }

    public void setPermissioncode(String permissioncode) {
        this.permissioncode = permissioncode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    @Override
    public String toString() {
        return "TblPermissionInfo{" +
                "permissioncode='" + permissioncode + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", enable='" + enable + '\'' +
                ", field1='" + field1 + '\'' +
                '}';
    }
}