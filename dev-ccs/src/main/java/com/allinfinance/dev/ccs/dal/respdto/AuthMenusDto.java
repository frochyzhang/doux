package com.allinfinance.dev.ccs.dal.respdto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @project: dev-parent
 * @description: 权限菜单树
 * @author: Lum Wang
 * @create: 2021-05-21 15:59
 */
public class AuthMenusDto implements Serializable {
    private String key;
    private String title;
    private String isUse;
    private String value;
    private String parentId;
    private ArrayList<AuthMenusDto> children;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public ArrayList<AuthMenusDto> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<AuthMenusDto> children) {
        this.children = children;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    @Override
    public String toString() {
        return "AuthMenusDto{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", isUse='" + isUse + '\'' +
                ", value='" + value + '\'' +
                ", parentId='" + parentId + '\'' +
                ", children=" + children +
                '}';
    }
}
