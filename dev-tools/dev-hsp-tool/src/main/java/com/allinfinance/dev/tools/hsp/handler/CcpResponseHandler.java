package com.allinfinance.dev.tools.hsp.handler;

import cn.hutool.crypto.SmUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-12-08 17:30
 */
public class CcpResponseHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CcpResponseHandler.class);

    private String appName;

    public CcpResponseHandler(String appName) {
        this.appName = appName;
    }

    public  void channelActive(){}
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String reqMsg = (String) msg;
        logger.info("收到请求消息:{}", reqMsg);
        String responseMsg1 ="<SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <SERVICE_HEADER>\n" +
                "    <SERVICE_SN>2022101910152998160</SERVICE_SN>\n" +
                "    <SERVICE_ID>11010</SERVICE_ID>\n" +
                "    <ORG>000066666666</ORG>\n" +
                "    <CHANNEL_ID>01</CHANNEL_ID>\n" +
                "    <OP_ID />\n" +
                "    <REQUST_TIME>20221019101529</REQUST_TIME>\n" +
                "    <VERSION_ID>01</VERSION_ID>\n" +
                "    <SERV_RESPONSE>\n" +
                "      <STATUS>S</STATUS>\n" +
                "      <CODE>SSSS</CODE>\n" +
                "      <DESC>交易成功</DESC>\n" +
                "    </SERV_RESPONSE>\n" +
                "    <RES_SERVICE_SN>2022101910152998160</RES_SERVICE_SN>\n" +
                "    <RES_SERVICE_TIME>20221019102500</RES_SERVICE_TIME>\n" +
                "  </SERVICE_HEADER>\n" +
                "  <SERVICE_BODY>\n" +
                "    <RESPONSE>\n" +
                "      <ID_NO>150101200105240088</ID_NO>\n" +
                "      <ID_TYPE>I</ID_TYPE>\n" +
                "      <TITLE/>\n" +
                "      <NAME>睡的好</NAME>\n" +
                "      <GENDER>F</GENDER>\n" +
                "      <BIRTHDAY>20010524</BIRTHDAY>\n" +
                "      <OCCUPATION>H</OCCUPATION>\n" +
                "      <OCCUPATION_SUB_TYPE/>\n" +
                "      <OCCUPATION_EXTEND/>\n" +
                "      <BANKMEMBER_NO/>\n" +
                "      <NATIONALITY>156</NATIONALITY>\n" +
                "      <MARITAL_STATUS>O</MARITAL_STATUS>\n" +
                "      <QUALIFICATION/>\n" +
                "      <HOME_PHONE/>\n" +
                "      <MOBILE_NO>15956888888</MOBILE_NO>\n" +
                "      <EMAIL>zzxvva@qq.com</EMAIL>\n" +
                "      <SETUP_DATE>20370622</SETUP_DATE>\n" +
                "      <CORP_NAME>未知</CORP_NAME>\n" +
                "      <DEPARTMENT/>\n" +
                "      <EMB_NAME>SHUI DEHAO</EMB_NAME>\n" +
                "      <BANK_CUSTOMER_ID/>\n" +
                "      <ID_START_DATE/>\n" +
                "      <ID_EXPIRE_DATE/>\n" +
                "      <ID_ISSUER_ADDR>内蒙古呼和浩特市市辖区</ID_ISSUER_ADDR>\n" +
                "      <CREATED_TIME>20210524155603</CREATED_TIME>\n" +
                "      <CUST_CAT_CD/>\n" +
                "      <COMP_POST>Z</COMP_POST>\n" +
                "      <COMP_STRUCTURE>Z</COMP_STRUCTURE>\n" +
                "      <COMP_TYPE>Z</COMP_TYPE>\n" +
                "      <COMP_TEL/>\n" +
                "      <COMP_FAX/>\n" +
                "      <EMP_STATUS>Y</EMP_STATUS>\n" +
                "      <CORP_BEGDATE/>\n" +
                "      <EMP_STABILITY>B</EMP_STABILITY>\n" +
                "      <SOCIAL_INS_AMT>0.00</SOCIAL_INS_AMT>\n" +
                "      <CORP_REVENUE_PER_YEAR>0.00</CORP_REVENUE_PER_YEAR>\n" +
                "      <LANGUAGE_IND/>\n" +
                "      <PR_OF_COUNTRY/>\n" +
                "      <RESIDENCY_COUNTRY_CD>156</RESIDENCY_COUNTRY_CD>\n" +
                "      <HOME_STAND_FROM/>\n" +
                "      <LIQUID_ASSET/>\n" +
                "      <HOUSE_TYPE/>\n" +
                "      <HOUSE_OWNERSHIP/>\n" +
                "      <CUST_ID>1145</CUST_ID>\n" +
                "      <IS_SET_CUSTOMER_PWD/>\n" +
                "      <CONTACT_FLAG>Y</CONTACT_FLAG>\n" +
                "      <ID_ISSUER/>\n" +
                "      <MKT_MSG_ALLOW/>\n" +
                "      <JOB_INFO/>\n" +
                "      <NATION/>\n" +
                "      <SOURCE_OF_INCOME/>\n" +
                "      <CASH_OVERSEA_BLACK_IND>N</CASH_OVERSEA_BLACK_IND>\n" +
                "    </RESPONSE>\n" +
                "  </SERVICE_BODY>\n" +
                "</SERVICE1111>\n";
        String responseMsg2 ="<SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <SERVICEddded_HEADER>\n" +
                "    <SERVICE_SN>2022101910152998160</SERVICE_SN>\n" +
                "    <SERVICE_ID>11010</SERVICE_ID>\n" +
                "    <ORG>000066666666</ORG>\n" +
                "    <CHANNEL_ID>01</CHANNEL_ID>\n" +
                "    <OP_ID />\n" +
                "    <REQUST_TIME>20221019101529</REQUST_TIME>\n" +
                "    <VERSION_ID>01</VERSION_ID>\n" +
                "    <SERV_RESPONSE>\n" +
                "      <STATUS>S</STATUS>\n" +
                "      <CODE>SSSS</CODE>\n" +
                "      <DESC>交易成功</DESC>\n" +
                "    </SERV_RESPONSE>\n" +
                "    <RES_SERVICE_SN>2022101910152998160</RES_SERVICE_SN>\n" +
                "    <RES_SERVICE_TIME>20221019102500</RES_SERVICE_TIME>\n" +
                "  </SERVICE_HEADER>\n" +
                "  <SERVICE_BODY>\n" +
                "    <RESPONSE>\n" +
                "      <ID_NO>150101200105240088</ID_NO>\n" +
                "      <ID_TYPE>I</ID_TYPE>\n" +
                "      <TITLE/>\n" +
                "      <NAME>睡的好</NAME>\n" +
                "      <GENDER>F</GENDER>\n" +
                "      <BIRTHDAY>20010524</BIRTHDAY>\n" +
                "      <OCCUPATION>H</OCCUPATION>\n" +
                "      <OCCUPATION_SUB_TYPE/>\n" +
                "      <OCCUPATION_EXTEND/>\n" +
                "      <BANKMEMBER_NO/>\n" +
                "      <NATIONALITY>156</NATIONALITY>\n" +
                "      <MARITAL_STATUS>O</MARITAL_STATUS>\n" +
                "      <QUALIFICATION/>\n" +
                "      <HOME_PHONE/>\n" +
                "      <MOBILE_NO>15956888888</MOBILE_NO>\n" +
                "      <EMAIL>zzxvva@qq.com</EMAIL>\n" +
                "      <SETUP_DATE>20370622</SETUP_DATE>\n" +
                "      <CORP_NAME>未知</CORP_NAME>\n" +
                "      <DEPARTMENT/>\n" +
                "      <EMB_NAME>SHUI DEHAO</EMB_NAME>\n" +
                "      <BANK_CUSTOMER_ID/>\n" +
                "      <ID_START_DATE/>\n" +
                "      <ID_EXPIRE_DATE/>\n" +
                "      <ID_ISSUER_ADDR>内蒙古呼和浩特市市辖区</ID_ISSUER_ADDR>\n" +
                "      <CREATED_TIME>20210524155603</CREATED_TIME>\n" +
                "      <CUST_CAT_CD/>\n" +
                "      <COMP_POST>Z</COMP_POST>\n" +
                "      <COMP_STRUCTURE>Z</COMP_STRUCTURE>\n" +
                "      <COMP_TYPE>Z</COMP_TYPE>\n" +
                "      <COMP_TEL/>\n" +
                "      <COMP_FAX/>\n" +
                "      <EMP_STATUS>Y</EMP_STATUS>\n" +
                "      <CORP_BEGDATE/>\n" +
                "      <EMP_STABILITY>B</EMP_STABILITY>\n" +
                "      <SOCIAL_INS_AMT>0.00</SOCIAL_INS_AMT>\n" +
                "      <CORP_REVENUE_PER_YEAR>0.00</CORP_REVENUE_PER_YEAR>\n" +
                "      <LANGUAGE_IND/>\n" +
                "      <PR_OF_COUNTRY/>\n" +
                "      <RESIDENCY_COUNTRY_CD>156</RESIDENCY_COUNTRY_CD>\n" +
                "      <HOME_STAND_FROM/>\n" +
                "      <LIQUID_ASSET/>\n" +
                "      <HOUSE_TYPE/>\n" +
                "      <HOUSE_OWNERSHIP/>\n" +
                "      <CUST_ID>1145</CUST_ID>\n" +
                "      <IS_SET_CUSTOMER_PWD/>\n" +
                "      <CONTACT_FLAG>Y</CONTACT_FLAG>\n" +
                "      <ID_ISSUER/>\n" +
                "      <MKT_MSG_ALLOW/>\n" +
                "      <JOB_INFO/>\n" +
                "      <NATION/>\n" +
                "      <SOURCE_OF_INCOME/>\n" +
                "      <CASH_OVERSEA_BLACK_IND>N</CASH_OVERSEA_BLACK_IND>\n" +
                "    </RESPONSE>\n" +
                "  </SERVICE_BODY>\n" +
                "</SERVICE>\n";
        if (reqMsg.hashCode()==9310836){
            ctx.writeAndFlush(responseMsg1);
            logger.info("返回应答消息:{}|{}", responseMsg1, responseMsg1.getBytes(StandardCharsets.UTF_8).length);

        }else if (reqMsg.hashCode()==288666749){
            ctx.writeAndFlush(responseMsg2);
            logger.info("返回应答消息:{}|{}", responseMsg2, responseMsg2.getBytes(StandardCharsets.UTF_8).length);
        }else {
            ctx.writeAndFlush("曾先后巴萨回家啊");
        }
//        logger.info("返回应答消息:{}|{}", respMsg, respMsg.getBytes(StandardCharsets.UTF_8).length);
//        ctx.writeAndFlush(respMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
