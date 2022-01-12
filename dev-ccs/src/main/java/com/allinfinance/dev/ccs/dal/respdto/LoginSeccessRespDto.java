package com.allinfinance.dev.ccs.dal.respdto;

import java.io.Serializable;

public class LoginSeccessRespDto implements Serializable {
    private String[] currentAuthority;
    private String QRcode;
    private String userId;
    private String userName;
    private String org;
    private String optFlag;
    private String passStatus;
    private String isFirstLogin;
    private String token;
    private String refreshToken;
    private long expirTime;

    public String getOptFlag() {
        return optFlag;
    }

    public void setOptFlag(String optFlag) {
        this.optFlag = optFlag;
    }

    public long getExpirTime() {
        return expirTime;
    }

    public void setExpirTime(long expirTime) {
        this.expirTime = expirTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String[] getCurrentAuthority() {
        return currentAuthority;
    }

    public void setCurrentAuthority(String[] currentAuthority) {
        this.currentAuthority = currentAuthority;
    }

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
        this.QRcode = QRcode;
    }

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

    public String getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(String passStatus) {
        this.passStatus = passStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
