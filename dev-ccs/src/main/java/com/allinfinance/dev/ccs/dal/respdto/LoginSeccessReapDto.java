package com.allinfinance.dev.ccs.dal.respdto;

import java.io.Serializable;

public class LoginSeccessReapDto  implements Serializable {
    private String [] currentAuthority;
    private String  QRcode;

    private String  userId;
    private String  userName;
    private String  passStatus;
    private String  token;

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
