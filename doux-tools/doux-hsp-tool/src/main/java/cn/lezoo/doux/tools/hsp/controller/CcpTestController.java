package cn.lezoo.doux.tools.hsp.controller;

import cn.lezoo.doux.tools.hsp.pojo.TestRequest;
import cn.lezoo.doux.tools.hsp.service.ccpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-12-08 16:26
 */
@RestController
public class CcpTestController {

    private static final Logger logger = LoggerFactory.getLogger(CcpTestController.class);

    @Autowired
    private ccpService ccpservice;

    @Value("${doux.tool.sleep-flag:false}")
    private Boolean sleepFlag;

    @PostMapping("/ccpTest")
    public Boolean ccpTest(@RequestBody TestRequest request) {
        logger.info("开始进行ccp调用！");
        String requestMsg1 = "<document>\" +\n" +
                "                \"<response>\" +\n" +
                "                \"<head>\" +\n" +
                "                \"<version>1.0.0</version>\" +\n" +
                "                \"<instId>HBC</instId>\" +\n" +
                "                \"<test>00001</test>\" +\n" +
                "                \"<function>ant.current.bankpoint.cccreq</function>\" +\n" +
                "                \"<respTime>20220615150531</respTime>\" +\n" +
                "                \"<certId>AIF2020072101</certId>\" +\n" +
                "                \"<reqMsgId>2022061500000000008253045910</reqMsgId>\" +\n" +
                "                \"<channelSystemId>HBC19</channelSystemId>\" +\n" +
                "                \"</head>\" +\n" +
                "                \"<body>\" +\n" +
                "                \"<accountName>月亮一</accountName>\" +";
        String requestMsg2 = "<document>\" +\n" +
                "                \"<response>\" +\n" +
                "                \"<head>\" +\n" +
                "                \"<version>1.0.0</version>\" +\n" +
                "                \"<instId>HBC</instId>\" +\n" +
                "                \"<test>00002</test>\" +\n" +
                "                \"<function>ant.current.bankpoint.cccreq</function>\" +\n" +
                "                \"<respTime>20220615150531</respTime>\" +\n" +
                "                \"<certId>AIF2020072101</certId>\" +\n" +
                "                \"<reqMsgId>2022061500000000008253045910</reqMsgId>\" +\n" +
                "                \"<channelSystemId>HBC19</channelSystemId>\" +\n" +
                "                \"</head>\" +\n" +
                "                \"<body>\" +\n" +
                "                \"<accountName>月亮一</accountName>\" +\n";
        if (sleepFlag) {
            try {
                Thread.sleep(10);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Boolean result = ccpservice.ccpTest(requestMsg1, requestMsg2);
        logger.info("返回结果为: {}", result);
        return result;
    }


}
