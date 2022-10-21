package com.allinfinance.dev.common.util.convert.common;

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

//    public static void main(String[] args) {
//        SensitiveDataConverter sensitiveConvert = new SensitiveDataConverter();
    //        //String str1 = "<root><MsgHeader><MsgVer>1000</MsgVer><SndDt>2020-06-30T17:37:27</SndDt><Trxtyp>0001</Trxtyp><IssrId>00010000</IssrId><Drctn>11</Drctn><SignSN>4000370686</SignSN><EncSN></EncSN><EncKey></EncKey><MDAlgo>0</MDAlgo><SignEncAlgo>0</SignEncAlgo><EncAlgo></EncAlgo></MsgHeader><MsgBody><BizTp>300001</BizTp><BizFunc>111011</BizFunc><BizAssInf><BizAssInfRsv></BizAssInfRsv></BizAssInf><TrxInf><TrxId>0630c0000013727a</TrxId><TrxDtTm>2020-06-30T17:37:27</TrxDtTm><SettlmtDt>2020-06-30</SettlmtDt><SettlmtInf>1723</SettlmtInf><TrxAmt></TrxAmt></TrxInf><RcverInf><RcverAcctIssrId>65561100</RcverAcctIssrId><RcverAcctId>1234567890128888</RcverAcctId><RcverAcctTp></RcverAcctTp><RcverNm>张小小</RcverNm><IDTp>01</IDTp><IDNo>123456789009870584</IDNo><MobNo>12345678901</MobNo></RcverInf><SensInf></SensInf><SderInf><SderIssrId>W0ACQ001</SderIssrId><SderAcctIssrId>W0ACQ001</SderAcctIssrId><SderAcctIssrNm>测试机构</SderAcctIssrNm></SderInf>";
//        //String str2 = "{RcverAcctIssrId='04839201', RcverAcctId='6283370000000440', RcverAcctTp='', RcverNm='巫倩妮', IDTp='01', IDNo='**************5262', MobNo='*******5678', AcctLvl='null', Smskey='null', AuthMsg='null', SgnNo='null'}, sderInf=SderInf{SderIssrId='W0ACQ001', SderAcctIssrId='W0ACQ002', SderAcctIssrNm='测>试机构', SderAcctInf='null', SderAcctId='6283370000000440', SderAcctTp='null'}";
//        //String str3 = "{Trxtyp='0001', TrxDtTm='2020-07-08T17:35:01', TxnCode='2001', SysRtnCd='null', SysRtnTm='null', TrxId='0708c0000033501a', SysSn='200708173548529335', SrcOrgId='W0ACQ002', BizTp='300001', CardNo='6283370000000440', cardType='null', SignNo='null', TrxAmt='', SettlmtDt='20200708', SettlmtInf='1723', RPFlg='null', AcctLvl='null', CustName='巫倩妮', IDTp='01', IDNo='**************5262', MobNo='*******5678', ExpireDate='null', CVN2='null', SderAcctInf='null', Smskey='null', OtpValue='null', AcctInTp='null', TrxTrmTp='null', TrxTrmNo='null', ProductTp='00000000', ProductAssInformation='null', ChannelIssrId='null', InstallmentCount='null', SderAcctIssrId='W0ACQ002', SderAcctIssrName='null', SderAcctId='6283370000000440', SderAcctType='null', SderAreaNo='null', sderName='撒贝宁'', RcvOrgId='04839201', RcvAcctIssrId='04839201', RcvAcctIssrName='null', RcvAcctId='1111111111110440', RcvAcctType='', RcvAreaNo='null', Rcvname='大声道'', MrchntNo='', MrchntPltfrmNm='', MrchntTpId='', SubMrchntNo='', SubMrchntPltfrmNm='', SubMrchntTpId='', OriTrxId='null', OrdrId='null', OrdrDesc='null', OverDraftFlag='null', SrcTime='20200708173501', PyerComment='null', BizFunc='111011', BizAssInfRsv='', PreAuthId='null', IPNum='null', IPMode='null', IPMrchntExpRate='null', IPCardHolderFee='null', IPCardHolderFeeMode='null', IPDownPaymentFee='null', IPEarchPaymentFee='null', IPDownPayment='null', IPMrchntActRate='null'}";
//        for (int i=0; i<20; i++){
////            String str4 = "TblTransLog{sysSn='200709104813529350', txnCode='2001', outerTxnCode='0001', txnStatus='P', qpsTxnDate='20180220', qpsSysDate='20200709', qpsSysTime='104813', srcSn='0709c0000154726a', srcOrgId='W0ACQ002', srcTime='20200709104726', destSn='200709104813529350', destOrgId='69551750', destTime='20200709104813', interfaceVersion='null', thirdPartyCode='11', bizType='300001', rpFlag='null', cardNo='6283370000000440', signNo='null', cardType='null', acctLvl='null', cust-Name='巫倩\n" +
////                    "妮', idType='01', idNo='null', phoneNo='13912345678', expireDate='null', cvn2='null', payAcctInfo='null', otpId='null', otpValue='null', acctInType='null', trxTermType='null', trxTermNo='null', productType='00000000', productAssInfo='null', channelIssId='null', transAmt=null, currency='null', fee=null, refundAmt=null, principalAmt=null, installmentCount=null, limitType='null', sderAcctIssrId='W0ACQ002', sderAcctIssrName='null', sderAcctId='null', sderAcctType='null', sderName='null', sderAreaNo='null', rcvAcctIssrId='05599201', rcvAcctIssrName='null', rcvAcctId='6283370000000440', rcvAcctType='', rcvName='巫倩妮', rcvAreaNo='null', mchtNo='', mchtName='', mchtType='', mchtCertType='null', mchtCertNo='null', mchtCategory='null', subMchtNo='null', subMchtName='', subMchtType='', oriSn='null', orderId='null', orderDetail='null', respCode='null', respDesc='null', coreResp='null', authIdResp='null', reserve60='null', flushFlag='Y', rcltFlag='N', overdraftFlag='null', settlmtDate='20200708', reserve1='20200709104726', reserve2='1023', reserve3='null', bizFunc='111011', bizAssInfRsv='', preAuthId='null', ipNum='null', ipMode='null', ipMrchntExpRate='null', ipCardHolderFee=null, ipDownPaymentFee=null, ipEachPaymentFee=null, ipDownPayment=null, ipMrchntActRate='null', pyerComment='null', preAuthStatus='null', cupnRespCode='null', ipCardHolderFeeMode='null'"
////            + "2域:323262833323232323232323270000000440, 42=md12323232323232323232323 261=01000000000000135262            CUPAM0010000100000000006巫倩妮>61域:02020202020202020202020202020202020202020202020202020D20202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020D6D0B9FAD2F8C1AACEDEBFA020//TEL='13912345678'-06-28 <TEL>13随机生成验证码：959988, 生成时间:20200720180506, 失效时间:20200720181006912345678</TEL>11:35:57,626 DEBUG [DubboServerHandler-10.250.20.207:44014-thread-37] c.a.q.u.s.codec.DIYMessageEncoder [DIYMessageEncoder.java : 47] 验证码959988，您尾号0440的信用卡正在开通银联无卡支付，请核实。如有疑问请致电0311-96369。编码前消息：<?xml version=\"1.0\" encoding=\"UTF-8\"?><SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\"><SERVICE_HEADER><SERVICE_SN>20200628113557529211</SERVICE_SN><SERVICE_ID>16050</SERVICE_ID><ORG>000064836560</ORG><CHANNEL_ID>06</CHANNEL_ID><REQUST_TIME>20200628113557</REQUST_TIME><VERSION_ID>01</VERSION_ID></SERVICE_HEADER><SERVICE_BODY><REQUEST></CARD_NO><ID_TYPE>I</ID_TYPE><ID_NO>422601198306135262</ID_NO><MOBILE_NO>13912345678</MOBILE_NO><CUST_NAME>巫倩妮</CUST_NAME></REQUEST></SERVICE_BODY></SERVICE>";
////            System.out.println("脱敏后：" + sensitiveConvert.invokeMsg(str4));
////        }
////    }

}
