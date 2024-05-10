package cn.lezoo.doux.common.util.convert.common;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class SensitiveDataConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        // 获取原始日志
        String oriLogMsg = event.getFormattedMessage();

        // 获取脱敏后的日志
        String afterLogMsg = invokeMsg(oriLogMsg);
        return afterLogMsg;
    }

    //日志脱敏
    private String invokeMsg(String oriLogMsg) {
        return oriLogMsg.replaceAll("(?i)<SderName>[\\s\\S]*</SderName>", "<SderName>***</SderName>")
                .replaceAll("(?i)<SderAcctId>\\w{12}", "<SderAcctId>************")
                .replaceAll("(?i)<RcverAcctId>\\w{12}", "<RcverAcctId>************")
                .replaceAll("(?i)<RcverNm>[\\s\\S]*</RcverNm>", "<RcverNm>***</RcverNm>")
                .replaceAll("(?i)<IDNo>\\w{14}", "<IDNo>**************")
                .replaceAll("(?i)<MobNo>\\d{7}", "<MobNo>*******")
                .replaceAll("(?i)SderName='[\\s\\S]*'", "SderName='***'")
                .replaceAll("(?i)SderAcctId='\\w{12}", "SderAcctId='************")
                .replaceAll("(?i)RcverAcctId='\\w{12}", "RcverAcctId='************")
                .replaceAll("(?i)RcvAcctId='\\w{12}", "RcvAcctId='************")
                .replaceAll("(?i)RcverNm='[\\s\\S]*'", "RcverNm='***'")
                .replaceAll("(?i)RcvName='[\\s\\S]*'", "RcvName='***'")
                .replaceAll("(?i)IDNo='\\w{14}", "IDNo='**************")
                .replaceAll("(?i)MobNo='\\w{7}", "MobNo='*******")
                .replaceAll("(?i)<MOB_NO>\\w{7}", "<MOB_NO>*******")
                .replaceAll("(?i)MOB_NO='\\w{7}", "MOB_NO='*******")
                .replaceAll("(?i)CustName='[\\s\\S]*'", "CustName='***'")
                .replaceAll("(?i)CardNo='\\w{12}", "CardNo='************")
                .replaceAll("[2]=\\d{12}", "2=***********")
                .replaceAll("[2]域:\\d{24}", "2域:************************")
                .replaceAll("[1][4]=\\d{4}", "14=****")
                .replaceAll("[1][4]域:\\d{8}", "14域:********")
                .replaceAll("[6][1]=(\\w*\\s*\\w*)([\\u4e00-\\u9fa5]*)", "61=$1***")
                .replaceAll("[6][1]域:(\\w*)(\\w{16})$", "61域:$1****************")
                .replaceAll("(?i)PyeeAcctId='\\w{12}", "PyeeAcctId='************")
                .replaceAll("(?i)<PyeeAcctId>\\w{12}", "<PyeeAcctId>************")
                .replaceAll("(?i)PyeeNm='[\\s\\S]*'", "PyeeNm='***'")
                .replaceAll("(?i)<PyeeNm>[\\s\\S]*</PyeeNm>", "<PyeeNm>***</PyeeNm>")
                .replaceAll("(?i)PyerAcctId='\\w{12}", "PyerAcctId='************")
                .replaceAll("(?i)<PyerAcctId>\\w{12}", "<PyerAcctId>************")
                .replaceAll("(?i)PyerNm='[\\s\\S]*'", "PyerNm='***'")
                .replaceAll("(?i)<PyerNm>[\\s\\S]*</PyerNm>", "<PyerNm>***</PyerNm>")
                .replaceAll("(?i)phoneNo='\\w{7}", "PhoneNo='*******")
                .replaceAll("(?i)<PHONE_NO>\\w{7}", "<PHONE_NO>*******")
                .replaceAll("(?i)<CUST_NAME>[\\s\\S]*</CUST_NAME>", "<CUST_NAME>***</CUST_NAME>")
                .replaceAll("(?i)SGN_ACCT_NM='[\\s\\S]*'", "SGN_ACCT_NM='***'")
                .replaceAll("(?i)<SGN_ACCT_NM>[\\s\\S]*</SGN_ACCT_NM>", "<SGN_ACCT_NM>***</SGN_ACCT_NM>")
                .replaceAll("(?i)<CARD_NO>\\w{12}", "<CARD_NO>************")
                .replaceAll("(?i)SGN_ACCT_ID='\\w{12}", "SGN_ACCT_ID='************")
                .replaceAll("(?i)<SGN_ACCT_ID>\\w{12}", "<SGN_ACCT_ID>************")
                .replaceAll("(?i)<ID_NO>\\w{14}", "<ID_NO>**************")
                .replaceAll("(?i)ID_NO='\\w{14}", "ID_NO='**************")
                .replaceAll("(?i)<MOBILE_NO>\\w{7}", "<MOBILE_NO>*******")
                .replaceAll("(?i)<TEL>\\w{7}", "<TEL>*******")
                .replaceAll("(?i)TEL='\\w{7}", "TEL='*******")
                .replaceAll("验证码\\d{6}", "验证码******")
                .replaceAll("验证码[\\s\\S]\\d{6}", "验证码:******");

    }
}
