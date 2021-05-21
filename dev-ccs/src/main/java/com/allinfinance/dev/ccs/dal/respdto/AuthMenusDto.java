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
    private String menuId;
    private String menuName;
    private String parentId;
    private ArrayList<AuthMenusDto> children;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
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

    @Override
    public String toString() {
        return "AuthMenusDto{" +
                "menuId='" + menuId + '\'' +
                ", menuName='" + menuName + '\'' +
                ", parentId='" + parentId + '\'' +
                ", children=" + children +
                '}';
    }
}
