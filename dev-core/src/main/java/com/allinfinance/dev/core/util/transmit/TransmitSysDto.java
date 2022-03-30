package com.allinfinance.dev.core.util.transmit;

import java.io.Serializable;

/**
 * TransmitSysDto
 *
 * @author hongmr
 * @date 2017/2/22
 */
public class TransmitSysDto implements Serializable {
    private String orgId;
    private String sysSource;
    private String transmitMode = "01";
    private String sysIp;
    private int sysPort;
    private String sysUser;
    private String sysPassword;
    private String defaultPath;

    @Override
    public String toString() {
        return "TransmitSysDto{" +
                "orgId='" + orgId + '\'' +
                ", sysSource='" + sysSource + '\'' +
                ", transmitMode='" + transmitMode + '\'' +
                ", sysIp='" + sysIp + '\'' +
                ", sysPort=" + sysPort +
                ", sysUser='" + sysUser + '\'' +
                ", sysPassword='******'" +
                ", defaultPath='" + defaultPath + '\'' +
                '}';
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSysSource() {
        return sysSource;
    }

    public void setSysSource(String sysSource) {
        this.sysSource = sysSource;
    }

    public String getTransmitMode() {
        return transmitMode;
    }

    public void setTransmitMode(String transmitMode) {
        this.transmitMode = transmitMode;
    }

    public String getSysIp() {
        return sysIp;
    }

    public void setSysIp(String sysIp) {
        this.sysIp = sysIp;
    }

    public int getSysPort() {
        return sysPort;
    }

    public void setSysPort(int sysPort) {
        this.sysPort = sysPort;
    }

    public String getSysUser() {
        return sysUser;
    }

    public void setSysUser(String sysUser) {
        this.sysUser = sysUser;
    }

    public String getSysPassword() {
        return sysPassword;
    }

    public void setSysPassword(String sysPassword) {
        this.sysPassword = sysPassword;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }
}

