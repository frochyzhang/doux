package com.allinfinance.dev.hsp;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.core.dto.hsp.HspBaseResponseDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyRequestDTO;
import com.allinfinance.dev.core.dto.hsp.SignatureGetBySM2PrivateKeyResponseDTO;
import com.allinfinance.dev.core.util.hsp.SignatureService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ImportResource;

import java.util.concurrent.TimeUnit;

/**
 * @author huanghf
 * @date 2022/6/23 14:35
 */
@State(Scope.Benchmark)
@EnableDubboConfig
@EnableDubbo
@SpringBootApplication(scanBasePackages = "com.allinfinance")
@ImportResource(locations = {"${dev.dubbo.files}"})
public class HspApplication {
    public static void main(String[] args) throws RunnerException {
        SpringApplication springApplication = new SpringApplicationBuilder(HspApplication.class).build(args);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run();
        Options opt = new OptionsBuilder()
                .include(HspApplication.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(3)
                .forks(0)
                .threads(8)
                .shouldDoGC(true)
                .timeout(TimeValue.minutes(5))
                .shouldFailOnError(false)
                .resultFormat(ResultFormatType.JSON)
                .result("./result.json")
                .jvmArgs("-server")
                .build();

        new Runner(opt).run();
    }

    @Autowired
    public void setSignatureService(SignatureService signatureService) {
        HspApplication.signatureService = signatureService;
    }

    private static SignatureService signatureService;

    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Benchmark
    public void getSignatureBySM2PrivateKey() {
        SignatureGetBySM2PrivateKeyRequestDTO requestDTO = new SignatureGetBySM2PrivateKeyRequestDTO();
        requestDTO.setPrivateKey("3400d3a8980730fe00b4ead82ef3d4a0f8c96fe2af3e15985bba0bd94a8a2c30a535421934d474a7b4479fcccf9c7c6cff531d498560f19589af92e521a56d5c188c6ed038d96340a6195be7b69b913c9a23b2dc1717c9d631041ebbd976a9a507de26258c0dd371f5cbb2768c16ccd5");
        requestDTO.setCertId(HexUtil.decodeHexStr("34303339383037353238"));
        requestDTO.setData("<MsgHeader><MsgVer>1000</MsgVer><SndDt>2022-06-20T13:41:06</SndDt><Trxtyp>0001</Trxtyp><IssrId>00010000</IssrId><Drctn>11</Drctn><SignSN>4039807528</SignSN><EncSN></EncSN><EncKey></EncKey><MDAlgo>1</MDAlgo><SignEncAlgo>1</SignEncAlgo><EncAlgo></EncAlgo></MsgHeader><MsgBody><BizTp>300001</BizTp><BizFunc>111011</BizFunc><BizAssInf><BizAssInfRsv>银联测试123</BizAssInfRsv></BizAssInf><TrxInf><TrxId>0620c0000474106a</TrxId><TrxDtTm>2022-06-20T13:41:06</TrxDtTm><SettlmtDt>2022-06-09</SettlmtDt><SettlmtInf>1323</SettlmtInf><TrxAmt></TrxAmt></TrxInf><RcverInf><RcverAcctIssrId>64901380</RcverAcctIssrId><RcverAcctId>6288888888888888</RcverAcctId><RcverAcctTp></RcverAcctTp><RcverNm>张小小</RcverNm><IDTp>01</IDTp><IDNo>410482198302100584</IDNo><MobNo>13201569405</MobNo></RcverInf><SensInf></SensInf><SderInf><SderIssrId>W0ACQ001</SderIssrId><SderAcctIssrId>W0ACQ002</SderAcctIssrId><SderAcctIssrNm>测试机构</SderAcctIssrNm></SderInf><CorpCard><CorpName></CorpName><USCCode></USCCode></CorpCard><ProductInf><ProductTp>00000000</ProductTp><ProductAssInformation></ProductAssInformation></ProductInf><MrchntInf><MrchntNo></MrchntNo><MrchntTpId></MrchntTpId><MrchntPltfrmNm></MrchntPltfrmNm></MrchntInf><SubMrchntInf><SubMrchntNo></SubMrchntNo><SubMrchntTpId></SubMrchntTpId><SubMrchntPltfrmNm></SubMrchntPltfrmNm></SubMrchntInf><RskInf><deviceMode></deviceMode><deviceLanguage></deviceLanguage><sourceIP></sourceIP><MAC></MAC><devId></devId><extensiveDeviceLocation></extensiveDeviceLocation><deviceNumber></deviceNumber><deviceSIMNumber></deviceSIMNumber><accountIDHash></accountIDHash><riskScore></riskScore><riskReasonCode></riskReasonCode><mchntUsrRgstrTm></mchntUsrRgstrTm><mchntUsrRgstrEmail></mchntUsrRgstrEmail><rcvProvince></rcvProvince><rcvCity></rcvCity><goodsClass></goodsClass></RskInf></MsgBody></root>");
        HspBaseResponseDTO<SignatureGetBySM2PrivateKeyResponseDTO> responseDTO = signatureService.getSignatureBySM2PrivateKey(requestDTO);
    }
}
