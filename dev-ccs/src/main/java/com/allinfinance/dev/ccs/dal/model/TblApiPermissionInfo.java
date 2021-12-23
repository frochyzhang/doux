package com.allinfinance.dev.ccs.dal.model;

public class TblApiPermissionInfo {
    private String permissioncode;

    private String url;

    private String description;

    private String enable;

    private String field1;

    public TblApiPermissionInfo(String permissioncode, String url, String description, String enable, String field1) {
        this.permissioncode = permissioncode;
        this.url = url;
        this.description = description;
        this.enable = enable;
        this.field1 = field1;
    }

    public TblApiPermissionInfo() {
        super();
    }

    public String getPermissioncode() {
        return permissioncode;
    }

    public void setPermissioncode(String permissioncode) {
        this.permissioncode = permissioncode == null ? null : permissioncode.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1 == null ? null : field1.trim();
    }
}