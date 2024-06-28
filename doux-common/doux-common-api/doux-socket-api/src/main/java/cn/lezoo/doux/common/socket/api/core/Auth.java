package cn.lezoo.doux.common.socket.api.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Auth
 *
 * @author hongmr
 * @date 2017/6/19.
 */
@Data
@Accessors(chain = true)
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
}
