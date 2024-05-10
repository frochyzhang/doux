package cn.lezoo.doux.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Auth
 *
 * @author hongmr
 * @date 2017/6/19.
 */
public class Auth {
    @JacksonXmlProperty(localName = "ID_TYPE")
    protected String idType;
    @JacksonXmlProperty(localName = "ID_NO")
    protected String idNo;
    @JacksonXmlProperty(localName = "MOBILE_NO")
    protected String mobileNo;
    @JacksonXmlProperty(localName = "HOME_PHONE")
    protected String homePhone;
    @JacksonXmlProperty(localName = "CUST_NAME")
    protected String custName;
    @JacksonXmlProperty(localName = "BIRTHDAY")
    protected String birthday;
    @JacksonXmlProperty(localName = "CORP_PHONE")
    protected String corpPhone;
    @JacksonXmlProperty(localName = "NAME")
    protected String name;
    @JacksonXmlProperty(localName = "PHONE")
    protected String phone;
    @JacksonXmlProperty(localName = "Q_PIN")
    protected String qPin;
    @JacksonXmlProperty(localName = "P_PIN")
    protected String pPin;

    public String getIdType() {
        return idType;
    }

    public Auth setIdType(String idType) {
        this.idType = idType;
        return this;
    }

    public String getIdNo() {
        return idNo;
    }

    public Auth setIdNo(String idNo) {
        this.idNo = idNo;
        return this;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public Auth setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        return this;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public Auth setHomePhone(String homePhone) {
        this.homePhone = homePhone;
        return this;
    }

    public String getCustName() {
        return custName;
    }

    public Auth setCustName(String custName) {
        this.custName = custName;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public Auth setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getCorpPhone() {
        return corpPhone;
    }

    public Auth setCorpPhone(String corpPhone) {
        this.corpPhone = corpPhone;
        return this;
    }

    public String getName() {
        return name;
    }

    public Auth setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Auth setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getqPin() {
        return qPin;
    }

    public Auth setqPin(String qPin) {
        this.qPin = qPin;
        return this;
    }

    public String getpPin() {
        return pPin;
    }

    public Auth setpPin(String pPin) {
        this.pPin = pPin;
        return this;
    }

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
}
