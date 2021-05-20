package com.allinfinance.dev.ccs.dal.paramvo;

import java.io.Serializable;

public class LoginParam implements Serializable {
    private String userName;
    private String userPass;
    private String bank;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
