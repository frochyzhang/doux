package cn.lezoo.doux.socket.server.tool.handler;

import cn.lezoo.doux.common.util.xml.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * @author huanghf
 * @date 2024/7/26 9:26
 */
@Slf4j
public class AbiipMinaServerHandler extends IoHandlerAdapter {
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.info("收到请求：{}", message);
        String serviceId = XmlUtils.getValueWithoutLabel((String) message, "SERVICE_ID");
        String resXml = "";
        switch (serviceId) {
            case "12000":
                resXml = "<SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <SERVICE_HEADER>\n" +
                        "    <SERVICE_SN>2024072609331059238</SERVICE_SN>\n" +
                        "    <SERVICE_ID>12000</SERVICE_ID>\n" +
                        "    <ORG>000005755202</ORG>\n" +
                        "    <CHANNEL_ID>01</CHANNEL_ID>\n" +
                        "    <OP_ID />\n" +
                        "    <REQUST_TIME>20240726093310</REQUST_TIME>\n" +
                        "    <VERSION_ID>01</VERSION_ID>\n" +
                        "    <SERV_RESPONSE>\n" +
                        "      <STATUS>S</STATUS>\n" +
                        "      <CODE>SSSS</CODE>\n" +
                        "      <DESC>交易成功</DESC>\n" +
                        "    </SERV_RESPONSE>\n" +
                        "    <RES_SERVICE_SN>2024072609331059238</RES_SERVICE_SN>\n" +
                        "    <RES_SERVICE_TIME>20240726093326</RES_SERVICE_TIME>\n" +
                        "  </SERVICE_HEADER>\n" +
                        "  <SERVICE_BODY>\n" +
                        "    <RESPONSE>\n" +
                        "      <CARD_NO>6258501170000692</CARD_NO>\n" +
                        "      <NAME>沙八</NAME>\n" +
                        "      <CURR_CD>156</CURR_CD>\n" +
                        "      <PRODUCT_NAME>随用金卡-捷德</PRODUCT_NAME>\n" +
                        "      <CREDIT_LIMIT>50000.00</CREDIT_LIMIT>\n" +
                        "      <TEMP_LIMIT>0.00</TEMP_LIMIT>\n" +
                        "      <TEMP_LIMIT_BEGIN_DATE/>\n" +
                        "      <TEMP_LIMIT_END_DATE/>\n" +
                        "      <CASH_LIMIT_RT>1.00</CASH_LIMIT_RT>\n" +
                        "      <OVRLMT_RATE>0.00</OVRLMT_RATE>\n" +
                        "      <LOAN_LIMIT_RT>1.00</LOAN_LIMIT_RT>\n" +
                        "      <CURR_BAL>26790.75</CURR_BAL>\n" +
                        "      <CASH_BAL>0.00</CASH_BAL>\n" +
                        "      <PRINCIPAL_BAL>0.00</PRINCIPAL_BAL>\n" +
                        "      <LOAN_BAL>0.00</LOAN_BAL>\n" +
                        "      <DISPUTE_AMT>0.00</DISPUTE_AMT>\n" +
                        "      <BEGIN_BAL>26790.75</BEGIN_BAL>\n" +
                        "      <PMT_DUE_DAY_BAL>0.00</PMT_DUE_DAY_BAL>\n" +
                        "      <QUAL_GRACE_BAL>26790.75</QUAL_GRACE_BAL>\n" +
                        "      <GRACE_DAYS_FULL_IND>N</GRACE_DAYS_FULL_IND>\n" +
                        "      <AVAILABLE_OTB>23209.25</AVAILABLE_OTB>\n" +
                        "      <ACCT_CASH_OTB>23209.25</ACCT_CASH_OTB>\n" +
                        "      <SETUP_DATE>20271129</SETUP_DATE>\n" +
                        "      <OWNING_BRANCH>99999</OWNING_BRANCH>\n" +
                        "      <BILLING_CYCLE>26</BILLING_CYCLE>\n" +
                        "      <STMT_FLAG>Y</STMT_FLAG>\n" +
                        "      <STMT_MAIL_ADDR_IND>H</STMT_MAIL_ADDR_IND>\n" +
                        "      <STMT_MEDIA_TYPE>E</STMT_MEDIA_TYPE>\n" +
                        "      <BLOCK_CODE>4D</BLOCK_CODE>\n" +
                        "      <AGE_CD>4</AGE_CD>\n" +
                        "      <UNMATCH_DB>0.00</UNMATCH_DB>\n" +
                        "      <UNMATCH_CASH>0.00</UNMATCH_CASH>\n" +
                        "      <UNMATCH_CR>0.00</UNMATCH_CR>\n" +
                        "      <DUAL_BILLING_FLAG>N</DUAL_BILLING_FLAG>\n" +
                        "      <LAST_PMT_AMT>0.00</LAST_PMT_AMT>\n" +
                        "      <LAST_PMT_DATE/>\n" +
                        "      <LAST_STMT_DATE>20280426</LAST_STMT_DATE>\n" +
                        "      <NEXT_STMT_DATE>20280526</NEXT_STMT_DATE>\n" +
                        "      <PMT_DUE_DATE>20280521</PMT_DUE_DATE>\n" +
                        "      <DD_DATE>20280520</DD_DATE>\n" +
                        "      <GRACE_DATE>20280524</GRACE_DATE>\n" +
                        "      <CLOSED_DATE/>\n" +
                        "      <FIRST_STMT_DATE>20271226</FIRST_STMT_DATE>\n" +
                        "      <CANCEL_DATE/>\n" +
                        "      <CHARGE_OFF_DATE/>\n" +
                        "      <FIRST_PURCHASE_DATE>20280104</FIRST_PURCHASE_DATE>\n" +
                        "      <FIRST_PURCHASE_AMT>300000.00</FIRST_PURCHASE_AMT>\n" +
                        "      <TOT_DUE_AMT>26790.75</TOT_DUE_AMT>\n" +
                        "      <CURR_TOT_DUE_AMT>26790.75</CURR_TOT_DUE_AMT>\n" +
                        "      <DUAL_CURR_IND>N</DUAL_CURR_IND>\n" +
                        "      <DUAL_CURR_CD/>\n" +
                        "      <CURR_REMAIN_TOT_BAL>26790.75</CURR_REMAIN_TOT_BAL>\n" +
                        "      <STMT_GRACE_BAL>26790.75</STMT_GRACE_BAL>\n" +
                        "      <LARGE_LOAN_BAL>0.00</LARGE_LOAN_BAL>\n" +
                        "      <TXN_LOAN_DAILY_RATE>0.001000</TXN_LOAN_DAILY_RATE>\n" +
                        "      <TXN_LOAN_DAILY_RATE_START_DATE>20271129</TXN_LOAN_DAILY_RATE_START_DATE>\n" +
                        "      <TXN_LOAN_DAILY_RATE_END_DATE>20301129</TXN_LOAN_DAILY_RATE_END_DATE>\n" +
                        "      <SHARED_FLAG>N</SHARED_FLAG>\n" +
                        "      <ACCT_NO>3823</ACCT_NO>\n" +
                        "      <OVER_PAYMENT_AMT>0.00</OVER_PAYMENT_AMT>\n" +
                        "      <OVER_DUE_DATE/>\n" +
                        "      <ACCT_OTB>23209.25</ACCT_OTB>\n" +
                        "    </RESPONSE>\n" +
                        "  </SERVICE_BODY>\n" +
                        "</SERVICE>";
                break;
            case "12010":
                resXml = "<SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <SERVICE_HEADER>\n" +
                        "    <SERVICE_SN>2024072610015206777</SERVICE_SN>\n" +
                        "    <SERVICE_ID>12010</SERVICE_ID>\n" +
                        "    <ORG>000005755202</ORG>\n" +
                        "    <CHANNEL_ID>01</CHANNEL_ID>\n" +
                        "    <OP_ID />\n" +
                        "    <REQUST_TIME>20240726100152</REQUST_TIME>\n" +
                        "    <VERSION_ID>01</VERSION_ID>\n" +
                        "    <SERV_RESPONSE>\n" +
                        "      <STATUS>S</STATUS>\n" +
                        "      <CODE>SSSS</CODE>\n" +
                        "      <DESC>交易成功</DESC>\n" +
                        "    </SERV_RESPONSE>\n" +
                        "    <RES_SERVICE_SN>2024072610015206777</RES_SERVICE_SN>\n" +
                        "    <RES_SERVICE_TIME>20240726100156</RES_SERVICE_TIME>\n" +
                        "  </SERVICE_HEADER>\n" +
                        "  <SERVICE_BODY>\n" +
                        "    <RESPONSE>\n" +
                        "      <CARD_NO>6258501170000692</CARD_NO>\n" +
                        "      <CURR_CD>156</CURR_CD>\n" +
                        "      <STMT_DATE>202804</STMT_DATE>\n" +
                        "      <BILLING_DATE>20280426</BILLING_DATE>\n" +
                        "      <NAME>沙八</NAME>\n" +
                        "      <PMT_DUE_DATE>20280521</PMT_DUE_DATE>\n" +
                        "      <CREDIT_LIMIT>50000</CREDIT_LIMIT>\n" +
                        "      <CASH_LIMIT>50000.00</CASH_LIMIT>\n" +
                        "      <TEMP_LIMIT>0</TEMP_LIMIT>\n" +
                        "      <TEMP_LIMIT_BEGIN_DATE/>\n" +
                        "      <TEMP_LIMIT_END_DATE/>\n" +
                        "      <LAST_STMT_DATE>20280326</LAST_STMT_DATE>\n" +
                        "      <STMT_BEG_BAL>25515.00</STMT_BEG_BAL>\n" +
                        "      <STMT_CURR_BAL>26790.75</STMT_CURR_BAL>\n" +
                        "      <CTD_CASH_AMT>0.00</CTD_CASH_AMT>\n" +
                        "      <QUAL_GRACE_BAL>26790.75</QUAL_GRACE_BAL>\n" +
                        "      <TOT_DUE_AMT>26790.75</TOT_DUE_AMT>\n" +
                        "      <CTD_AMT_DB>1275.75</CTD_AMT_DB>\n" +
                        "      <CTD_NBR_DB>1</CTD_NBR_DB>\n" +
                        "      <CTD_AMT_CR>0.00</CTD_AMT_CR>\n" +
                        "      <CTD_NBR_CR>0</CTD_NBR_CR>\n" +
                        "      <AGE_CD>4</AGE_CD>\n" +
                        "      <GRACE_DAYS_FULL_IND>N</GRACE_DAYS_FULL_IND>\n" +
                        "      <POINT_BEGIN_BAL>0</POINT_BEGIN_BAL>\n" +
                        "      <CTD_EARNED_POINTS>0</CTD_EARNED_POINTS>\n" +
                        "      <CTD_ADJ_POINTS>0</CTD_ADJ_POINTS>\n" +
                        "      <CTD_DISB_POINTS>0</CTD_DISB_POINTS>\n" +
                        "      <POINT_BAL>0</POINT_BAL>\n" +
                        "      <DUAL_CURR_IND>N</DUAL_CURR_IND>\n" +
                        "      <DUAL_CURR_CD/>\n" +
                        "      <CTD_PMT_YET_AMT>0.00</CTD_PMT_YET_AMT>\n" +
                        "      <CTD_PMT_NOT_AMT>26790.75</CTD_PMT_NOT_AMT>\n" +
                        "      <CTD_FEE_AMT>1275.75</CTD_FEE_AMT>\n" +
                        "      <CTD_FEE_CNT>1</CTD_FEE_CNT>\n" +
                        "      <CTD_INTEREST_AMT>0.00</CTD_INTEREST_AMT>\n" +
                        "      <CTD_INTEREST_CNT>0</CTD_INTEREST_CNT>\n" +
                        "    </RESPONSE>\n" +
                        "  </SERVICE_BODY>\n" +
                        "</SERVICE>";
                break;
        }
        session.write(resXml);
    }
}
