package com.allinfinance.dev.ccs.dal.paramvo;

import com.allinfinance.dev.ccs.dal.model.TblBankManage;

import java.util.Arrays;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/5/19 13:49
 * @description：
 */
public class BankManageReqParam extends BaseReqParam {

    private String bankId;
    private String[] bankIds;
    private String bankName;
    private String bankNameEn;
    private String isAvailable;
    private String org;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String[] getBankIds() {
        return bankIds;
    }

    public void setBankIds(String[] bankIds) {
        this.bankIds = bankIds;
    }

    public String getBankNameEn() {
        return bankNameEn;
    }

    public void setBankNameEn(String bankNameEn) {
        this.bankNameEn = bankNameEn;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "BankManageReqParam{" +
                "bankId='" + bankId + '\'' +
                ", bankIds=" + Arrays.toString(bankIds) +
                ", bankName='" + bankName + '\'' +
                ", bankNameEn='" + bankNameEn + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", org='" + org + '\'' +
                '}';
    }
}
