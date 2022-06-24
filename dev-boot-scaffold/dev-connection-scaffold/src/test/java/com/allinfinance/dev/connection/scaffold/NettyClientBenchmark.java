package com.allinfinance.dev.connection.scaffold;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.connection.scaffold.pool.PoolManager;
import com.allinfinance.dev.connection.scaffold.pool.QueueManger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author qipeng
 * @date 2022/6/17 16:17
 * @description
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
@SpringBootTest(classes = ConnectionPoolAutoConfiguration.class)
public class NettyClientBenchmark extends AbstractBenchmark {
//public class NettyClientBenchmark {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientBenchmark.class);
    private static PoolManager poolManager;
    private static QueueManger queueManger;


    @Autowired
    private void setPoolManager(PoolManager poolManager, QueueManger queueManger) {
        NettyClientBenchmark.poolManager = poolManager;
        NettyClientBenchmark.queueManger = queueManger;
    }

//    @Benchmark
//    public void poolTest() throws Exception {
//        //发送请求
//        String msg = HexUtil.encodeHexStr(UUID.fastUUID().toString());
//        String s = queueManger.writeAndFlush(msg);
//        Assertions.assertEquals(msg, s);
//        logger.info(s);
//    }

    public void queueTest() {
        String d30A = new StringBuffer("D30A")
                .append("06")
                .append("0003")
                .append("313233").toString();
//        logger.info(queueManger.writeAndFlush("d30a0607253c726f6f743e3c4d73674865616465723e3c4d73675665723e313030303c2f4d73675665723e3c536e6444743e323032322d30362d32305431333a34313a30363c2f536e6444743e3c5472787479703e303030313c2f5472787479703e3c4973737249643e30303031303030303c2f4973737249643e3c447263746e3e31313c2f447263746e3e3c5369676e534e3e343033393830373532383c2f5369676e534e3e3c456e63534e3e3c2f456e63534e3e3c456e634b65793e3c2f456e634b65793e3c4d44416c676f3e313c2f4d44416c676f3e3c5369676e456e63416c676f3e313c2f5369676e456e63416c676f3e3c456e63416c676f3e3c2f456e63416c676f3e3c2f4d73674865616465723e3c4d7367426f64793e3c42697a54703e3330303030313c2f42697a54703e3c42697a46756e633e3131313031313c2f42697a46756e633e3c42697a417373496e663e3c42697a417373496e665273763ee993b6e88194e6b58be8af953132333c2f42697a417373496e665273763e3c2f42697a417373496e663e3c547278496e663e3c54727849643e303632306330303030343734313036613c2f54727849643e3c5472784474546d3e323032322d30362d32305431333a34313a30363c2f5472784474546d3e3c536574746c6d7444743e323032322d30362d30393c2f536574746c6d7444743e3c536574746c6d74496e663e313332333c2f536574746c6d74496e663e3c547278416d743e3c2f547278416d743e3c2f547278496e663e3c5263766572496e663e3c5263766572416363744973737249643e36343930313338303c2f5263766572416363744973737249643e3c52637665724163637449643e363238383838383838383838383838383c2f52637665724163637449643e3c52637665724163637454703e3c2f52637665724163637454703e3c52637665724e6d3ee5bca0e5b08fe5b08f3c2f52637665724e6d3e3c494454703e30313c2f494454703e3c49444e6f3e3431303438323139383330323130303538343c2f49444e6f3e3c4d6f624e6f3e31333230313536393430353c2f4d6f624e6f3e3c2f5263766572496e663e3c53656e73496e663e3c2f53656e73496e663e3c53646572496e663e3c536465724973737249643e57304143513030313c2f536465724973737249643e3c53646572416363744973737249643e57304143513030323c2f53646572416363744973737249643e3c5364657241636374497373724e6d3ee6b58be8af95e69cbae69e843c2f5364657241636374497373724e6d3e3c2f53646572496e663e3c436f7270436172643e3c436f72704e616d653e3c2f436f72704e616d653e3c555343436f64653e3c2f555343436f64653e3c2f436f7270436172643e3c50726f64756374496e663e3c50726f6475637454703e30303030303030303c2f50726f6475637454703e3c50726f64756374417373496e666f726d6174696f6e3e3c2f50726f64756374417373496e666f726d6174696f6e3e3c2f50726f64756374496e663e3c4d7263686e74496e663e3c4d7263686e744e6f3e3c2f4d7263686e744e6f3e3c4d7263686e74547049643e3c2f4d7263686e74547049643e3c4d7263686e74506c7466726d4e6d3e3c2f4d7263686e74506c7466726d4e6d3e3c2f4d7263686e74496e663e3c5375624d7263686e74496e663e3c5375624d7263686e744e6f3e3c2f5375624d7263686e744e6f3e3c5375624d7263686e74547049643e3c2f5375624d7263686e74547049643e3c5375624d7263686e74506c7466726d4e6d3e3c2f5375624d7263686e74506c7466726d4e6d3e3c2f5375624d7263686e74496e663e3c52736b496e663e3c6465766963654d6f64653e3c2f6465766963654d6f64653e3c6465766963654c616e67756167653e3c2f6465766963654c616e67756167653e3c736f7572636549503e3c2f736f7572636549503e3c4d41433e3c2f4d41433e3c64657649643e3c2f64657649643e3c657874656e736976654465766963654c6f636174696f6e3e3c2f657874656e736976654465766963654c6f636174696f6e3e3c6465766963654e756d6265723e3c2f6465766963654e756d6265723e3c64657669636553494d4e756d6265723e3c2f64657669636553494d4e756d6265723e3c6163636f756e744944486173683e3c2f6163636f756e744944486173683e3c7269736b53636f72653e3c2f7269736b53636f72653e3c7269736b526561736f6e436f64653e3c2f7269736b526561736f6e436f64653e3c6d63686e745573725267737472546d3e3c2f6d63686e745573725267737472546d3e3c6d63686e745573725267737472456d61696c3e3c2f6d63686e745573725267737472456d61696c3e3c72637650726f76696e63653e3c2f72637650726f76696e63653e3c726376436974793e3c2f726376436974793e3c676f6f6473436c6173733e3c2f676f6f6473436c6173733e3c2f52736b496e663e3c2f4d7367426f64793e3c2f726f6f743e"));
        String resp = queueManger.writeAndFlush(d30A);
        Assertions.assertAll(
                () -> Assertions.assertEquals("4100206e0f9e14344c5406a0cf5a3b4dfb665f87f4a771a31f7edbb5c72874a32b2957", resp)
//                () -> logger.info(resp)
        );
    }

    @Benchmark
    public void test_digest_sign_verify() {
        String respStr = "<allslasdlkasjdlfasdf><MsgHeader><MsgVer>1000</MsgVer><SndDt>2022-06-20T13:41:06</SndDt><Trxtyp>0001</Trxtyp><IssrId>00010000</IssrId><Drctn>11</Drctn><SignSN>4039807528</SignSN><EncSN></EncSN><EncKey></EncKey><MDAlgo>1</MDAlgo><SignEncAlgo>1</SignEncAlgo><EncAlgo></EncAlgo></MsgHeader><MsgBody><BizTp>300001</BizTp><BizFunc>111011</BizFunc><BizAssInf><BizAssInfRsv>银联测试123</BizAssInfRsv></BizAssInf><TrxInf><TrxId>0620c0000474106a</TrxId><TrxDtTm>2022-06-20T13:41:06</TrxDtTm><SettlmtDt>2022-06-09</SettlmtDt><SettlmtInf>1323</SettlmtInf><TrxAmt></TrxAmt></TrxInf><RcverInf><RcverAcctIssrId>64901380</RcverAcctIssrId><RcverAcctId>6288888888888888</RcverAcctId><RcverAcctTp></RcverAcctTp><RcverNm>张小小</RcverNm><IDTp>01</IDTp><IDNo>410482198302100584</IDNo><MobNo>13201569405</MobNo></RcverInf><SensInf></SensInf><SderInf><SderIssrId>W0ACQ001</SderIssrId><SderAcctIssrId>W0ACQ002</SderAcctIssrId><SderAcctIssrNm>测试机构</SderAcctIssrNm></SderInf><CorpCard><CorpName></CorpName><USCCode></USCCode></CorpCard><ProductInf><ProductTp>00000000</ProductTp><ProductAssInformation></ProductAssInformation></ProductInf><MrchntInf><MrchntNo></MrchntNo><MrchntTpId></MrchntTpId><MrchntPltfrmNm></MrchntPltfrmNm></MrchntInf><SubMrchntInf><SubMrchntNo></SubMrchntNo><SubMrchntTpId></SubMrchntTpId><SubMrchntPltfrmNm></SubMrchntPltfrmNm></SubMrchntInf><RskInf><deviceMode></deviceMode><deviceLanguage></deviceLanguage><sourceIP></sourceIP><MAC></MAC><devId></devId><extensiveDeviceLocation></extensiveDeviceLocation><deviceNumber></deviceNumber><deviceSIMNumber></deviceSIMNumber><accountIDHash></accountIDHash><riskScore></riskScore><riskReasonCode></riskReasonCode><mchntUsrRgstrTm></mchntUsrRgstrTm><mchntUsrRgstrEmail></mchntUsrRgstrEmail><rcvProvince></rcvProvince><rcvCity></rcvCity><goodsClass></goodsClass></RskInf></MsgBody></root>";

        String digest = getHash(respStr);
        String d306 = new StringBuffer("d306ffff")
                .append("0070")
                .append("3400d3a8980730fe00b4ead82ef3d4a0f8c96fe2af3e15985bba0bd94a8a2c30a535421934d474a7b4479fcccf9c7c6cff531d498560f19589af92e521a56d5c188c6ed038d96340a6195be7b69b913c9a23b2dc1717c9d631041ebbd976a9a507de26258c0dd371f5cbb2768c16ccd5")
                .append("0702")
                .append("000a34303339383037353238")
                .append("0020")
                .append(digest).toString();

        String d306Resp = queueManger.writeAndFlush(d306);
        if (!d306Resp.startsWith("41")) {
            logger.error(d306Resp);
        }
        Assertions.assertTrue(d306Resp.startsWith("41"));

//        digest = getHash(respStr);
//        String d307 = new StringBuffer("d307ffff")
//                .append("1b232da2af19440372e4a942af5c403ee7e374e27365109330c3abd5bfcdb16da4185c3edf420f5bb21d86c8e70e3c3ff76ec72abf4ab614279769d5acf42312")
////                .append(d306Resp.substring(d306Resp.length() - 64 * 2))
//                .append("cf85e3833b2928f187760e8915e9a57b34240102d33043cda365ed396d04d4cfbbe1b8d5ab37b1f0cc7795a854cee15b6b8562467176ce735c4cb3245942d995")
//                .append("02")
//                .append("000a34303339383037353238")
//                .append("0020")
//                .append(digest).toString();
//
//        String d307Resp = queueManger.writeAndFlush(d307);
//        if (!d307Resp.startsWith("41")) {
//            logger.error(d307Resp);
//        }
//
//        Assertions.assertTrue(d307Resp.startsWith("41"));
    }

    private String getHash(String msg) {
        String str = HexUtil.encodeHexStr(msg);
        String d30A = new StringBuffer("D30A")
                .append("06")
                .append(String.format("%04x", str.length() / 2))
                .append(str).toString();

        String resp = queueManger.writeAndFlush(d30A);

        Assertions.assertTrue(resp.startsWith("41"));
        return resp.substring(resp.length() - 32 * 2);
    }

    @Test
    public void test_d306() {
        String d30A = new StringBuffer("D30A")
                .append("06")
                .append("0003")
                .append("313233").toString();
        logger.info(queueManger.writeAndFlush("d306ffff00703400d3a8980730fe00b4ead82ef3d4a0f8c96fe2af3e15985bba0bd94a8a2c30a535421934d474a7b4479fcccf9c7c6cff531d498560f19589af92e521a56d5c188c6ed038d96340a6195be7b69b913c9a23b2dc1717c9d631041ebbd976a9a507de26258c0dd371f5cbb2768c16ccd50702000a34303339383037353238002008070eb24feae78c8269e35b47de47320d6be1b4fdcfdcb0f3cc5895abfba0f1"));
    }
//    4100206e0f9e14344c5406a0cf5a3b4dfb665f87f4a771a31f7edbb5c72874a32b2957
//
//    @Test
//    public void test_multiThread() throws InterruptedException {
//        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("yuyee-", false);
//
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024),
//                namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
//
//        int LOOP_COUNT = 100;
//
//        CountDownLatch latch = new CountDownLatch(LOOP_COUNT);
//        for (int i = 0; i < LOOP_COUNT; i++) {
//            executor.submit(() -> {
//                logger.info(poolManager.writeAndFlush("3132333435"));
//                latch.countDown();
//            });
//        }
//
//        latch.await();
//        executor.shutdown();
//    }
}
