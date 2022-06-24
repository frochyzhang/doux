package com.allinfinance.dev.connection.scaffold;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.connection.scaffold.pool.MessagePorter;
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

    private static final Logger logger = LoggerFactory.getLogger(NettyClientBenchmark.class);

    private static MessagePorter messagePorter;

    @Autowired
    void setMessagePorter(MessagePorter messagePorter) {
        NettyClientBenchmark.messagePorter = messagePorter;
    }

    @Benchmark
    public void test_digest_sign() {
        String respStr = "<allslasdlkasjdlfasdf><MsgHeader><MsgVer>1000</MsgVer><SndDt>2022-06-20T13:41:06</SndDt><Trxtyp>0001</Trxtyp><IssrId>00010000</IssrId><Drctn>11</Drctn><SignSN>4039807528</SignSN><EncSN></EncSN><EncKey></EncKey><MDAlgo>1</MDAlgo><SignEncAlgo>1</SignEncAlgo><EncAlgo></EncAlgo></MsgHeader><MsgBody><BizTp>300001</BizTp><BizFunc>111011</BizFunc><BizAssInf><BizAssInfRsv>银联测试123</BizAssInfRsv></BizAssInf><TrxInf><TrxId>0620c0000474106a</TrxId><TrxDtTm>2022-06-20T13:41:06</TrxDtTm><SettlmtDt>2022-06-09</SettlmtDt><SettlmtInf>1323</SettlmtInf><TrxAmt></TrxAmt></TrxInf><RcverInf><RcverAcctIssrId>64901380</RcverAcctIssrId><RcverAcctId>6288888888888888</RcverAcctId><RcverAcctTp></RcverAcctTp><RcverNm>张小小</RcverNm><IDTp>01</IDTp><IDNo>410482198302100584</IDNo><MobNo>13201569405</MobNo></RcverInf><SensInf></SensInf><SderInf><SderIssrId>W0ACQ001</SderIssrId><SderAcctIssrId>W0ACQ002</SderAcctIssrId><SderAcctIssrNm>测试机构</SderAcctIssrNm></SderInf><CorpCard><CorpName></CorpName><USCCode></USCCode></CorpCard><ProductInf><ProductTp>00000000</ProductTp><ProductAssInformation></ProductAssInformation></ProductInf><MrchntInf><MrchntNo></MrchntNo><MrchntTpId></MrchntTpId><MrchntPltfrmNm></MrchntPltfrmNm></MrchntInf><SubMrchntInf><SubMrchntNo></SubMrchntNo><SubMrchntTpId></SubMrchntTpId><SubMrchntPltfrmNm></SubMrchntPltfrmNm></SubMrchntInf><RskInf><deviceMode></deviceMode><deviceLanguage></deviceLanguage><sourceIP></sourceIP><MAC></MAC><devId></devId><extensiveDeviceLocation></extensiveDeviceLocation><deviceNumber></deviceNumber><deviceSIMNumber></deviceSIMNumber><accountIDHash></accountIDHash><riskScore></riskScore><riskReasonCode></riskReasonCode><mchntUsrRgstrTm></mchntUsrRgstrTm><mchntUsrRgstrEmail></mchntUsrRgstrEmail><rcvProvince></rcvProvince><rcvCity></rcvCity><goodsClass></goodsClass></RskInf></MsgBody></root>";

        String digest = getHash(respStr);
        String d306 = new StringBuffer("d306ffff")
                .append("0070")
                .append("3400d3a8980730fe00b4ead82ef3d4a0f8c96fe2af3e15985bba0bd94a8a2c30a535421934d474a7b4479fcccf9c7c6cff531d498560f19589af92e521a56d5c188c6ed038d96340a6195be7b69b913c9a23b2dc1717c9d631041ebbd976a9a507de26258c0dd371f5cbb2768c16ccd5")
                .append("0702")
                .append("000a34303339383037353238")
                .append("0020")
                .append(digest).toString();

        String d306Resp = messagePorter.writeAndFlush(d306);
        Assertions.assertTrue(d306Resp.startsWith("41"));

    }

    @Benchmark
    public void test_digest_verify() {
        String respStr = "<allslasdlkasjdlfasdf><MsgHeader><MsgVer>1000</MsgVer><SndDt>2022-06-20T13:41:06</SndDt><Trxtyp>0001</Trxtyp><IssrId>00010000</IssrId><Drctn>11</Drctn><SignSN>4039807528</SignSN><EncSN></EncSN><EncKey></EncKey><MDAlgo>1</MDAlgo><SignEncAlgo>1</SignEncAlgo><EncAlgo></EncAlgo></MsgHeader><MsgBody><BizTp>300001</BizTp><BizFunc>111011</BizFunc><BizAssInf><BizAssInfRsv>银联测试123</BizAssInfRsv></BizAssInf><TrxInf><TrxId>0620c0000474106a</TrxId><TrxDtTm>2022-06-20T13:41:06</TrxDtTm><SettlmtDt>2022-06-09</SettlmtDt><SettlmtInf>1323</SettlmtInf><TrxAmt></TrxAmt></TrxInf><RcverInf><RcverAcctIssrId>64901380</RcverAcctIssrId><RcverAcctId>6288888888888888</RcverAcctId><RcverAcctTp></RcverAcctTp><RcverNm>张小小</RcverNm><IDTp>01</IDTp><IDNo>410482198302100584</IDNo><MobNo>13201569405</MobNo></RcverInf><SensInf></SensInf><SderInf><SderIssrId>W0ACQ001</SderIssrId><SderAcctIssrId>W0ACQ002</SderAcctIssrId><SderAcctIssrNm>测试机构</SderAcctIssrNm></SderInf><CorpCard><CorpName></CorpName><USCCode></USCCode></CorpCard><ProductInf><ProductTp>00000000</ProductTp><ProductAssInformation></ProductAssInformation></ProductInf><MrchntInf><MrchntNo></MrchntNo><MrchntTpId></MrchntTpId><MrchntPltfrmNm></MrchntPltfrmNm></MrchntInf><SubMrchntInf><SubMrchntNo></SubMrchntNo><SubMrchntTpId></SubMrchntTpId><SubMrchntPltfrmNm></SubMrchntPltfrmNm></SubMrchntInf><RskInf><deviceMode></deviceMode><deviceLanguage></deviceLanguage><sourceIP></sourceIP><MAC></MAC><devId></devId><extensiveDeviceLocation></extensiveDeviceLocation><deviceNumber></deviceNumber><deviceSIMNumber></deviceSIMNumber><accountIDHash></accountIDHash><riskScore></riskScore><riskReasonCode></riskReasonCode><mchntUsrRgstrTm></mchntUsrRgstrTm><mchntUsrRgstrEmail></mchntUsrRgstrEmail><rcvProvince></rcvProvince><rcvCity></rcvCity><goodsClass></goodsClass></RskInf></MsgBody></root>";

        String digest = getHash(respStr);
        String d307 = new StringBuffer("d307ffff")
                .append("1b232da2af19440372e4a942af5c403ee7e374e27365109330c3abd5bfcdb16da4185c3edf420f5bb21d86c8e70e3c3ff76ec72abf4ab614279769d5acf42312")
                .append("c1ac6473f93a10628b573c0e131c5aaec8b9537ddf6cd0288e2f87c07641ac6d0506007cff9009a122e6be3a3f4b24c75565be0f9388a477ed687a754025f563")
                //.append(d306Resp.substring(d306Resp.length() - 64 * 2))
                .append("02")
                .append("000a34303339383037353238")
                .append("0020")
                .append(digest).toString();

        String d307Resp = messagePorter.writeAndFlush(d307);
        Assertions.assertTrue(d307Resp.startsWith("41"));
    }


    @Test
    public void test_digest() {
        String respStr = "<allslasdlkasjdlfasdf><MsgHeader><MsgVer>1000</MsgVer><SndDt>2022-06-20T13:41:06</SndDt><Trxtyp>0001</Trxtyp><IssrId>00010000</IssrId><Drctn>11</Drctn><SignSN>4039807528</SignSN><EncSN></EncSN><EncKey></EncKey><MDAlgo>1</MDAlgo><SignEncAlgo>1</SignEncAlgo><EncAlgo></EncAlgo></MsgHeader><MsgBody><BizTp>300001</BizTp><BizFunc>111011</BizFunc><BizAssInf><BizAssInfRsv>银联测试123</BizAssInfRsv></BizAssInf><TrxInf><TrxId>0620c0000474106a</TrxId><TrxDtTm>2022-06-20T13:41:06</TrxDtTm><SettlmtDt>2022-06-09</SettlmtDt><SettlmtInf>1323</SettlmtInf><TrxAmt></TrxAmt></TrxInf><RcverInf><RcverAcctIssrId>64901380</RcverAcctIssrId><RcverAcctId>6288888888888888</RcverAcctId><RcverAcctTp></RcverAcctTp><RcverNm>张小小</RcverNm><IDTp>01</IDTp><IDNo>410482198302100584</IDNo><MobNo>13201569405</MobNo></RcverInf><SensInf></SensInf><SderInf><SderIssrId>W0ACQ001</SderIssrId><SderAcctIssrId>W0ACQ002</SderAcctIssrId><SderAcctIssrNm>测试机构</SderAcctIssrNm></SderInf><CorpCard><CorpName></CorpName><USCCode></USCCode></CorpCard><ProductInf><ProductTp>00000000</ProductTp><ProductAssInformation></ProductAssInformation></ProductInf><MrchntInf><MrchntNo></MrchntNo><MrchntTpId></MrchntTpId><MrchntPltfrmNm></MrchntPltfrmNm></MrchntInf><SubMrchntInf><SubMrchntNo></SubMrchntNo><SubMrchntTpId></SubMrchntTpId><SubMrchntPltfrmNm></SubMrchntPltfrmNm></SubMrchntInf><RskInf><deviceMode></deviceMode><deviceLanguage></deviceLanguage><sourceIP></sourceIP><MAC></MAC><devId></devId><extensiveDeviceLocation></extensiveDeviceLocation><deviceNumber></deviceNumber><deviceSIMNumber></deviceSIMNumber><accountIDHash></accountIDHash><riskScore></riskScore><riskReasonCode></riskReasonCode><mchntUsrRgstrTm></mchntUsrRgstrTm><mchntUsrRgstrEmail></mchntUsrRgstrEmail><rcvProvince></rcvProvince><rcvCity></rcvCity><goodsClass></goodsClass></RskInf></MsgBody></root>";
        String str = HexUtil.encodeHexStr(respStr);
        String d30A = new StringBuffer("D30A")
                .append("06")
                .append(String.format("%04x", str.length() / 2))
                .append(str).toString();

        String resp = messagePorter.writeAndFlush(d30A);

        Assertions.assertTrue(resp.startsWith("41"));

    }

    private String getHash(String msg) {
        String str = HexUtil.encodeHexStr(msg);
        String d30A = new StringBuffer("D30A")
                .append("06")
                .append(String.format("%04x", str.length() / 2))
                .append(str).toString();

        String resp = messagePorter.writeAndFlush(d30A);

        Assertions.assertTrue(resp.startsWith("41"));
        return resp.substring(resp.length() - 32 * 2);
    }
}
