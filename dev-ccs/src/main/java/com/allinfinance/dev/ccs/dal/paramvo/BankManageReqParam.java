package com.allinfinance.dev.ccs.dal.paramvo;

import com.allinfinance.dev.ccs.dal.model.TblBankManage;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/5/19 13:49
 * @description：
 */
public class BankManageReqParam extends TblBankManage {
    private Integer current;
    private Integer pageSize;
    private String[] bankIds;

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

    public String[] getBankIds() {
        return bankIds;
    }

    public void setBankIds(String[] bankIds) {
        this.bankIds = bankIds;
    }
}
