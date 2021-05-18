package com.allinfinance.dev.ccs.dal.paramvo;

import com.allinfinance.dev.ccs.dal.model.TblUser;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/5/14 10:16
 * @description：用户页面请求参数
 */

public class UserReqParam extends TblUser {
    private Integer current;
    private Integer pageSize;
    private Integer[] userIds;

    public Integer[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Integer[] userIds) {
        this.userIds = userIds;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
