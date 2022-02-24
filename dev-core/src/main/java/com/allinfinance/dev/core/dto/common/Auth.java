package com.allinfinance.dev.core.dto.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Auth
 *
 * @author hongmr
 * @date 2017/6/19.
 */
@XStreamAlias("AUTH")
public class Auth implements Serializable {
    @XStreamAlias("ID_TYPE")
    protected String idType;
    @XStreamAlias("ID_NO")
    protected String idNo;
    @XStreamAlias("MOBILE_NO")
    protected String mobileNo;
    @XStreamAlias("HOME_PHONE")
    protected String homePhone;
    @XStreamAlias("CUST_NAME")
    protected String custName;
    @XStreamAlias("BIRTHDAY")
    protected String birthday;
    @XStreamAlias("CORP_PHONE")
    protected String corpPhone;
    @XStreamAlias("NAME")
    protected String name;
    @XStreamAlias("PHONE")
    protected String phone;
    @XStreamAlias("Q_PIN")
    protected String qPin;
    @XStreamAlias("P_PIN")
    protected String pPin;

    @Override
    public String toString() {
        return "Auth{" +
                "idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", homePhone='" + homePhone + '\'' +
                ", custName='" + custName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", corpPhone='" + corpPhone + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", qPin='" + qPin + '\'' +
                ", pPin='" + pPin + '\'' +
                '}';
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCorpPhone() {
        return corpPhone;
    }

    public void setCorpPhone(String corpPhone) {
        this.corpPhone = corpPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getqPin() {
        return qPin;
    }

    public void setqPin(String qPin) {
        this.qPin = qPin;
    }

    public String getpPin() {
        return pPin;
    }

    public void setpPin(String pPin) {
        this.pPin = pPin;
    }
}
