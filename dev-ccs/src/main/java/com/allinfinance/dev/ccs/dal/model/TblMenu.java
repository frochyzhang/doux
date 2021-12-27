package com.allinfinance.dev.ccs.dal.model;

import java.util.Date;

public class TblMenu {
    private String menuId;

    private String menuName;

    private String parentMid;

    private String nodeType;

    private String icon;

    private String sort;

    private String pageUrl;

    private String level;

    private String path;

    private String isAvailable;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private String reservedField1;

    private String reservedField2;

    private String reservedField3;

    private String org;

    public TblMenu(String menuId, String menuName, String parentMid, String nodeType, String icon, String sort, String pageUrl, String level, String path, String isAvailable, Date createTime, String createBy, Date updateTime, String updateBy, String reservedField1, String reservedField2, String reservedField3, String org) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.parentMid = parentMid;
        this.nodeType = nodeType;
        this.icon = icon;
        this.sort = sort;
        this.pageUrl = pageUrl;
        this.level = level;
        this.path = path;
        this.isAvailable = isAvailable;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
        this.reservedField1 = reservedField1;
        this.reservedField2 = reservedField2;
        this.reservedField3 = reservedField3;
        this.org = org;
    }

    public TblMenu() {
        super();
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getParentMid() {
        return parentMid;
    }

    public void setParentMid(String parentMid) {
        this.parentMid = parentMid == null ? null : parentMid.trim();
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType == null ? null : nodeType.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl == null ? null : pageUrl.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable == null ? null : isAvailable.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public String getReservedField1() {
        return reservedField1;
    }

    public void setReservedField1(String reservedField1) {
        this.reservedField1 = reservedField1 == null ? null : reservedField1.trim();
    }

    public String getReservedField2() {
        return reservedField2;
    }

    public void setReservedField2(String reservedField2) {
        this.reservedField2 = reservedField2 == null ? null : reservedField2.trim();
    }

    public String getReservedField3() {
        return reservedField3;
    }

    public void setReservedField3(String reservedField3) {
        this.reservedField3 = reservedField3 == null ? null : reservedField3.trim();
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org == null ? null : org.trim();
    }
}