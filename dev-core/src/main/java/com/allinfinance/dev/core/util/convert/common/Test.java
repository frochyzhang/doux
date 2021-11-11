//package com.allinfinance.dev.core.util.convert.common;
//
//import java.util.concurrent.CyclicBarrier;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class Test {
//
//    public static void main(String[] args) throws InterruptedException {
//        long start = System.currentTimeMillis();
//        int count = 10000;
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(1000);
//
//        ExecutorService executorService = Executors.newFixedThreadPool(count);
//
//        for (int i = 0; i < 100000; i++) {
//            if (!executorService.isShutdown()) {
//                executorService.execute(new Test().new Task(cyclicBarrier));
//            }
//
//        }
//        executorService.shutdown();
//        executorService.awaitTermination(1, TimeUnit.HOURS);
//        long sum = System.currentTimeMillis() - start;
//        System.out.println("sum time:" + sum);
//    }
//
//    private class Task implements Runnable {
//        private CyclicBarrier cyclicBarrier;
//
//        public Task(CyclicBarrier cyclicBarrier) {
//            this.cyclicBarrier = cyclicBarrier;
//        }
//
//        @Override
//        public void run() {
//
//            try {
//                cyclicBarrier.await();
//                SensitiveDataConverter sensitiveConvert = new SensitiveDataConverter();
//                String str4 = "TblTransLog{sysSn='200709104813529350', txnCode='2001', outerTxnCode='0001', txnStatus='P', qpsTxnDate='20180220', qpsSysDate='20200709', qpsSysTime='104813', srcSn='0709c0000154726a', srcOrgId='W0ACQ002', srcTime='20200709104726', destSn='200709104813529350', destOrgId='69551750', destTime='20200709104813', interfaceVersion='null', thirdPartyCode='11', bizType='300001', rpFlag='null', cardNo='6283370000000440', signNo='null', cardType='null', acctLvl='null', cust-Name='巫倩\n" +
//                        "妮', idType='01', idNo='null', phoneNo='13912345678', expireDate='null', cvn2='null', payAcctInfo='null', otpId='null', otpValue='null', acctInType='null', trxTermType='null', trxTermNo='null', productType='00000000', productAssInfo='null', channelIssId='null', transAmt=null, currency='null', fee=null, refundAmt=null, principalAmt=null, installmentCount=null, limitType='null', sderAcctIssrId='W0ACQ002', sderAcctIssrName='null', sderAcctId='null', sderAcctType='null', sderName='null', sderAreaNo='null', rcvAcctIssrId='05599201', rcvAcctIssrName='null', rcvAcctId='6283370000000440', rcvAcctType='', rcvName='巫倩妮', rcvAreaNo='null', mchtNo='', mchtName='', mchtType='', mchtCertType='null', mchtCertNo='null', mchtCategory='null', subMchtNo='null', subMchtName='', subMchtType='', oriSn='null', orderId='null', orderDetail='null', respCode='null', respDesc='null', coreResp='null', authIdResp='null', reserve60='null', flushFlag='Y', rcltFlag='N', overdraftFlag='null', settlmtDate='20200708', reserve1='20200709104726', reserve2='1023', reserve3='null', bizFunc='111011', bizAssInfRsv='', preAuthId='null', ipNum='null', ipMode='null', ipMrchntExpRate='null', ipCardHolderFee=null, ipDownPaymentFee=null, ipEachPaymentFee=null, ipDownPayment=null, ipMrchntActRate='null', pyerComment='null', preAuthStatus='null', cupnRespCode='null', ipCardHolderFeeMode='null'"
//                        + "2域:323262833323232323232323270000000440, 42=md12323232323232323232323 261=01000000000000135262            CUPAM0010000100000000006巫倩妮>61域:02020202020202020202020202020202020202020202020202020D20202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020D6D0B9FAD2F8C1AACEDEBFA020//TEL='13912345678'-06-28 <TEL>13随机生成验证码：959988, 生成时间:20200720180506, 失效时间:20200720181006912345678</TEL>11:35:57,626 DEBUG [DubboServerHandler-10.250.20.207:44014-thread-37] c.a.q.u.s.codec.DIYMessageEncoder [DIYMessageEncoder.java : 47] 验证码959988，您尾号0440的信用卡正在开通银联无卡支付，请核实。如有疑问请致电0311-96369。编码前消息：<?xml version=\"1.0\" encoding=\"UTF-8\"?><SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\"><SERVICE_HEADER><SERVICE_SN>20200628113557529211</SERVICE_SN><SERVICE_ID>16050</SERVICE_ID><ORG>000064836560</ORG><CHANNEL_ID>06</CHANNEL_ID><REQUST_TIME>20200628113557</REQUST_TIME><VERSION_ID>01</VERSION_ID></SERVICE_HEADER><SERVICE_BODY><REQUEST></CARD_NO><ID_TYPE>I</ID_TYPE><ID_NO>422601198306135262</ID_NO><MOBILE_NO>13912345678</MOBILE_NO><CUST_NAME>巫倩妮</CUST_NAME></REQUEST></SERVICE_BODY></SERVICE>";
//                //sensitiveConvert.invokeMsg(str4);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
