package com.allinfinance.dev.ccs.dal.respdto;

import java.io.Serializable;

public class LogoutSeccessReapDto implements Serializable {

    private String  userId;
    private String  userName;
    private String  token;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
