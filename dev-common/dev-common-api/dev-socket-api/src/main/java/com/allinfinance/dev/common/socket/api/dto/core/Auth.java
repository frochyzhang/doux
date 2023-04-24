package com.allinfinance.dev.common.socket.api.dto.core;

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

    public Auth() {
    }

    public Auth(String idType, String idNo, String mobileNo, String homePhone, String custName, String birthday, String corpPhone, String name, String phone, String qPin, String pPin) {
        this.idType = idType;
        this.idNo = idNo;
        this.mobileNo = mobileNo;
        this.homePhone = homePhone;
        this.custName = custName;
        this.birthday = birthday;
        this.corpPhone = corpPhone;
        this.name = name;
        this.phone = phone;
        this.qPin = qPin;
        this.pPin = pPin;
    }

    public static AuthBuilder builder() {
        return new AuthBuilder();
    }

    public static class AuthBuilder {
        private String idType;
        private String idNo;
        private String mobileNo;
        private String homePhone;
        private String custName;
        private String birthday;
        private String corpPhone;
        private String name;
        private String phone;
        private String qPin;
        private String pPin;

        AuthBuilder() {
        }

        public AuthBuilder idType(String idType) {
            this.idType = idType;
            return this;
        }

        public AuthBuilder idNo(String idNo) {
            this.idNo = idNo;
            return this;
        }

        public AuthBuilder mobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
            return this;
        }

        public AuthBuilder homePhone(String homePhone) {
            this.homePhone = homePhone;
            return this;
        }

        public AuthBuilder custName(String custName) {
            this.custName = custName;
            return this;
        }

        public AuthBuilder birthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public AuthBuilder corpPhone(String corpPhone) {
            this.corpPhone = corpPhone;
            return this;
        }

        public AuthBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AuthBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public AuthBuilder qPin(String qPin) {
            this.qPin = qPin;
            return this;
        }

        public AuthBuilder pPin(String pPin) {
            this.pPin = pPin;
            return this;
        }

        public Auth build() {
            return new Auth(this.idType, this.idNo, this.mobileNo, this.homePhone, this.custName, this.birthday, this.corpPhone, this.name, this.phone, this.qPin, this.pPin);
        }

        public String toString() {
            return "Auth.AuthBuilder(idType=" + this.idType + ", idNo=" + this.idNo + ", mobileNo=" + this.mobileNo + ", homePhone=" + this.homePhone + ", custName=" + this.custName + ", birthday=" + this.birthday + ", corpPhone=" + this.corpPhone + ", name=" + this.name + ", phone=" + this.phone + ", qPin=" + this.qPin + ", pPin=" + this.pPin + ")";
        }
    }
}
