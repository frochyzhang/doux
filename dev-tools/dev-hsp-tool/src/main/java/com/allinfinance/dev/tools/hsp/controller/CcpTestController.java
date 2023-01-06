package com.allinfinance.dev.tools.hsp.controller;

import com.allinfinance.dev.tools.hsp.pojo.TestRequest;
import com.allinfinance.dev.tools.hsp.service.ccpService;
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

    @Value("${dev.tool.flag:false}")
    private Boolean flag;

    @PostMapping("/ccpTest")
    public Boolean ccpTest(@RequestBody TestRequest request) {
        logger.info("开始进行ccp调用！");
        String requestMsg1 = "<document>\" +\n" +
                "                \"<response>\" +\n" +
                "                \"<head>\" +\n" +
                "                \"<version>1.0.0</version>\" +\n" +
                "                \"<instId>HBC</instId>\" +\n" +
                "                \"<function>ant.current.bankpoint.cccreq</function>\" +\n" +
                "                \"<respTime>20220615150531</respTime>\" +\n" +
                "                \"<certId>AIF2020072101</certId>\" +\n" +
                "                \"<reqMsgId>2022061500000000008253045910</reqMsgId>\" +\n" +
                "                \"<channelSystemId>HBC19</channelSystemId>\" +\n" +
                "                \"</head>\" +\n" +
                "                \"<body>\" +\n" +
                "                \"<accountName>月亮一</accountName>\" +\n" +
                "                \"<cardNo>************0293</cardNo>\" +\n" +
                "                \"<resultInfo>\" +\n" +
                "                \"<resultStatus>S</resultStatus>\" +\n" +
                "                \"<resultCode>0000</resultCode>\" +\n" +
                "                \"<resultMsg>交易成功</resultMsg>\" +\n" +
                "                \"</resultInfo>\" +\n" +
                "                \"<extension/>\" +\n" +
                "                \"</body>\" +\n" +
                "                \"</response>\" +\n" +
                "                \"<signature>\" +\n" +
                "                \"ViDg0jB08br95YiowX4kAhSw25btiQMSZAJ7CiJAlpTIAGMNpBu5OlkAVX9AwP9b+Xo6BO1JWf7q2t7hCNVGzdXxiJZ3sV98/Ank9zaysnxdqdN9WvZl0uJ/XNR+llH4FPw/UcPG5yUlWlsJC2nllpc67AzaE3cxWrWV/nDTCTNuIoWAUwe3peLna5SjFaYr7+P+hqYEZs5+FfZyhfRB0ymdYYC/CF50yVtKD2ajgpdw0vXbYlgYz9/UCyzt+8CELO7tnXVchAnrV2IQ6ccYId2x3SaayUvaCKvZZYZ/U9sr06TJakdXgJF6uQCuWjw4djGhO4l1PeqrP8XH9D5Amw==</signature></document>]\";111";
        String requestMsg2 = "<document>\" +\n" +
                "                \"<response>\" +\n" +
                "                \"<head>\" +\n" +
                "                \"<version>1.0.0</version>\" +\n" +
                "                \"<instId>HBC</instId>\" +\n" +
                "                \"<function>ant.current.bankpoint.cccreq</function>\" +\n" +
                "                \"<respTime>20220615150531</respTime>\" +\n" +
                "                \"<certId>AIF2020072101</certId>\" +\n" +
                "                \"<reqMsgId>2022061500000000008253045910</reqMsgId>\" +\n" +
                "                \"<channelSystemId>HBC19</channelSystemId>\" +\n" +
                "                \"</head>\" +\n" +
                "                \"<body>\" +\n" +
                "                \"<accountName>月亮一</accountName>\" +\n" +
                "                \"<cardNo>************0293</cardNo>\" +\n" +
                "                \"<resultInfo>\" +\n" +
                "                \"<resultStatus>S</resultStatus>\" +\n" +
                "                \"<resultCode>0000</resultCode>\" +\n" +
                "                \"<resultMsg>交易成功</resultMsg>\" +\n" +
                "                \"</resultInfo>\" +\n" +
                "                \"<extension/>\" +\n" +
                "                \"</body>\" +\n" +
                "                \"</response>\" +\n" +
                "                \"<signature>\" +\n" +
                "                \"ViDg0jB08br95YiowX4kAhSw25btiQMSZAJ7CiJAlpTIAGMNpBu5OlkAVX9AwP9b+Xo6BO1JWf7q2t7hCNVGzdXxiJZ3sV98/Ank9zaysnxdqdN9WvZl0uJ/XNR+llH4FPw/UcPG5yUlWlsJC2nllpc67AzaE3cxWrWV/nDTCTNuIoWAUwe3peLna5SjFaYr7+P+hqYEZs5+FfZyhfRB0ymdYYC/CF50yVtKD2ajgpdw0vXbYlgYz9/UCyzt+8CELO7tnXVchAnrV2IQ6ccYId2x3SaayUvaCKvZZYZ/U9sr06TJakdXgJF6uQCuWjw4djGhO4l1PeqrP8XH9D5Amw==</signature></document>]\";2222";
        if (flag){
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
