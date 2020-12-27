package com.allinfinance.dev.socket.test;

import com.allinfinance.dev.core.util.socket.client.SocketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author 张勇
 * @description
 * @date 2020/12/27 18:46
 */
@RunWith(JUnit4.class)
public class TestSocketClient {

    @Test
    public void test() {
        try {
            SocketService client = new SocketService();
            String reqMess = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<SMS>\n" +
                    "    <ORG_ID>000064163056</ORG_ID>\n" +
                    "    <SMS_NO>113133000326</SMS_NO>\n" +
                    "    <SMS_TYPE>00</SMS_TYPE>\n" +
                    "    <TEL>13585961521</TEL>\n" +
                    "    <CONTENT>您好,您用于开通快捷支付的验证码为[226230].</CONTENT>\n" +
                    "    <REQUEST_TIME>20170612113133</REQUEST_TIME>\n" +
                    "    <RESERVED></RESERVED>\n" +
                    "</SMS>";
            String response1 = client.clientRequest("127.0.0.1", 4493, "sms", 30, false, reqMess, 6, "UTF-8");
            assert response1.contains("【服务端响应数据】");
            System.out.println("resultResp:" + response1);
        } finally {
            System.out.println("end");
        }
    }
}
