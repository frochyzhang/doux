package com.allinfinance.dev.ccs.dal.paramvo;

import com.allinfinance.dev.ccs.dal.model.TblMenu;

import java.util.Arrays;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/5/14 10:16
 * @description：用户页面请求参数
 */

public class MenusReqParam extends BaseReqParam {

    private String[] menusId;
    private String menuId;
    private String menuName;
    private String isAvailable;
    private String nodeType;

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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String[] getMenusId() {
        return menusId;
    }

    public void setMenusId(String[] menusId) {
        this.menusId = menusId;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "MenusReqParam{" +
                "menusId=" + Arrays.toString(menusId) +
                ", menuId='" + menuId + '\'' +
                ", menuName='" + menuName + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", nodeType='" + nodeType + '\'' +
                '}';
    }
}
