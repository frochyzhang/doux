package com.allinfinance.dev.hsp.service;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyRequestDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyResponseDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureVerifyBySM2PublicKeyRequestDTO;
import com.allinfinance.dev.core.util.hsp.SignatureService;
import com.allinfinance.dev.hsp.AbstractBenchmark;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
@SpringBootTest
public class SignatureServiceImplTest extends AbstractBenchmark {

    @Autowired
    public void setSignatureService(SignatureService signatureService) {
        SignatureServiceImplTest.signatureService = signatureService;
    }

    private static SignatureService signatureService;

    @Benchmark
    public void getSignatureBySM2PrivateKey() {
        SignatureGetBySM2PrivateKeyRequestDTO requestDTO = new SignatureGetBySM2PrivateKeyRequestDTO();
        requestDTO.setPrivateKey("3400d3a8980730fe00b4ead82ef3d4a0f8c96fe2af3e15985bba0bd94a8a2c30a535421934d474a7b4479fcccf9c7c6cff531d498560f19589af92e521a56d5c188c6ed038d96340a6195be7b69b913c9a23b2dc1717c9d631041ebbd976a9a507de26258c0dd371f5cbb2768c16ccd5");
        requestDTO.setCertId(HexUtil.decodeHexStr("34303339383037353238"));
        requestDTO.setData("<MsgHeader><MsgVer>1000</MsgVer><SndDt>2022-06-20T13:41:06</SndDt><Trxtyp>0001</Trxtyp><IssrId>00010000</IssrId><Drctn>11</Drctn><SignSN>4039807528</SignSN><EncSN></EncSN><EncKey></EncKey><MDAlgo>1</MDAlgo><SignEncAlgo>1</SignEncAlgo><EncAlgo></EncAlgo></MsgHeader><MsgBody><BizTp>300001</BizTp><BizFunc>111011</BizFunc><BizAssInf><BizAssInfRsv>银联测试123</BizAssInfRsv></BizAssInf><TrxInf><TrxId>0620c0000474106a</TrxId><TrxDtTm>2022-06-20T13:41:06</TrxDtTm><SettlmtDt>2022-06-09</SettlmtDt><SettlmtInf>1323</SettlmtInf><TrxAmt></TrxAmt></TrxInf><RcverInf><RcverAcctIssrId>64901380</RcverAcctIssrId><RcverAcctId>6288888888888888</RcverAcctId><RcverAcctTp></RcverAcctTp><RcverNm>张小小</RcverNm><IDTp>01</IDTp><IDNo>410482198302100584</IDNo><MobNo>13201569405</MobNo></RcverInf><SensInf></SensInf><SderInf><SderIssrId>W0ACQ001</SderIssrId><SderAcctIssrId>W0ACQ002</SderAcctIssrId><SderAcctIssrNm>测试机构</SderAcctIssrNm></SderInf><CorpCard><CorpName></CorpName><USCCode></USCCode></CorpCard><ProductInf><ProductTp>00000000</ProductTp><ProductAssInformation></ProductAssInformation></ProductInf><MrchntInf><MrchntNo></MrchntNo><MrchntTpId></MrchntTpId><MrchntPltfrmNm></MrchntPltfrmNm></MrchntInf><SubMrchntInf><SubMrchntNo></SubMrchntNo><SubMrchntTpId></SubMrchntTpId><SubMrchntPltfrmNm></SubMrchntPltfrmNm></SubMrchntInf><RskInf><deviceMode></deviceMode><deviceLanguage></deviceLanguage><sourceIP></sourceIP><MAC></MAC><devId></devId><extensiveDeviceLocation></extensiveDeviceLocation><deviceNumber></deviceNumber><deviceSIMNumber></deviceSIMNumber><accountIDHash></accountIDHash><riskScore></riskScore><riskReasonCode></riskReasonCode><mchntUsrRgstrTm></mchntUsrRgstrTm><mchntUsrRgstrEmail></mchntUsrRgstrEmail><rcvProvince></rcvProvince><rcvCity></rcvCity><goodsClass></goodsClass></RskInf></MsgBody></root>");
        SignatureGetBySM2PrivateKeyResponseDTO responseDTO = signatureService.getSignatureBySM2PrivateKey(requestDTO);
        Assertions.assertTrue(responseDTO.getSuccess());
    }

    @Test
    public void verifySignatureBySM2PublicKey() {
        SignatureVerifyBySM2PublicKeyRequestDTO requestDTO = new SignatureVerifyBySM2PublicKeyRequestDTO();
        requestDTO.setPlainPublicKeyX("1b232da2af19440372e4a942af5c403ee7e374e27365109330c3abd5bfcdb16d");
        requestDTO.setPlainPublicKeyY("a4185c3edf420f5bb21d86c8e70e3c3ff76ec72abf4ab614279769d5acf42312");
        requestDTO.setSignatureR("ab2be08082bb8e00c72719bb25bc827f87ff2472a4e246da59b83acddd25ee9a");
        requestDTO.setSignatureS("e74349334cd930fc9739d9dbed5e0a562f41f9864424b70b43776b92cfaaf090");
        requestDTO.setCertId(HexUtil.decodeHexStr("34303339383037353238"));
        requestDTO.setData("<MsgHeader><MsgVer>1000</MsgVer><SndDt>2022-06-20T13:41:06</SndDt><Trxtyp>0001</Trxtyp><IssrId>00010000</IssrId><Drctn>11</Drctn><SignSN>4039807528</SignSN><EncSN></EncSN><EncKey></EncKey><MDAlgo>1</MDAlgo><SignEncAlgo>1</SignEncAlgo><EncAlgo></EncAlgo></MsgHeader><MsgBody><BizTp>300001</BizTp><BizFunc>111011</BizFunc><BizAssInf><BizAssInfRsv>银联测试123</BizAssInfRsv></BizAssInf><TrxInf><TrxId>0620c0000474106a</TrxId><TrxDtTm>2022-06-20T13:41:06</TrxDtTm><SettlmtDt>2022-06-09</SettlmtDt><SettlmtInf>1323</SettlmtInf><TrxAmt></TrxAmt></TrxInf><RcverInf><RcverAcctIssrId>64901380</RcverAcctIssrId><RcverAcctId>6288888888888888</RcverAcctId><RcverAcctTp></RcverAcctTp><RcverNm>张小小</RcverNm><IDTp>01</IDTp><IDNo>410482198302100584</IDNo><MobNo>13201569405</MobNo></RcverInf><SensInf></SensInf><SderInf><SderIssrId>W0ACQ001</SderIssrId><SderAcctIssrId>W0ACQ002</SderAcctIssrId><SderAcctIssrNm>测试机构</SderAcctIssrNm></SderInf><CorpCard><CorpName></CorpName><USCCode></USCCode></CorpCard><ProductInf><ProductTp>00000000</ProductTp><ProductAssInformation></ProductAssInformation></ProductInf><MrchntInf><MrchntNo></MrchntNo><MrchntTpId></MrchntTpId><MrchntPltfrmNm></MrchntPltfrmNm></MrchntInf><SubMrchntInf><SubMrchntNo></SubMrchntNo><SubMrchntTpId></SubMrchntTpId><SubMrchntPltfrmNm></SubMrchntPltfrmNm></SubMrchntInf><RskInf><deviceMode></deviceMode><deviceLanguage></deviceLanguage><sourceIP></sourceIP><MAC></MAC><devId></devId><extensiveDeviceLocation></extensiveDeviceLocation><deviceNumber></deviceNumber><deviceSIMNumber></deviceSIMNumber><accountIDHash></accountIDHash><riskScore></riskScore><riskReasonCode></riskReasonCode><mchntUsrRgstrTm></mchntUsrRgstrTm><mchntUsrRgstrEmail></mchntUsrRgstrEmail><rcvProvince></rcvProvince><rcvCity></rcvCity><goodsClass></goodsClass></RskInf></MsgBody></root>");
        boolean verifyStatus = signatureService.verifySignatureBySM2PublicKey(requestDTO);
        Assertions.assertTrue(verifyStatus);
    }
}