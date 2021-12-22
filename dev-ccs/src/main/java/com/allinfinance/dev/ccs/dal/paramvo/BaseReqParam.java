package com.allinfinance.dev.ccs.dal.paramvo;

/**
 * @author ：lqf
 * @project :dev-parent
 * @date ：2021/12/20 15:55
 * @description：基础请求参数
 */
public class BaseReqParam {
    private Integer current;
    private Integer pageSize;

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

    @Override
    public String toString() {
        return "BaseReqParam{" +
                "current=" + current +
                ", pageSize=" + pageSize +
                '}';
    }
}
