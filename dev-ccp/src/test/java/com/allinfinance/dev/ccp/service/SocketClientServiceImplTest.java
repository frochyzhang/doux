package com.allinfinance.dev.ccp.service;

import cn.hutool.core.net.NetUtil;
import com.allinfinance.dev.core.util.socket.client.ISocketClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootTest
class SocketClientServiceImplTest {
    @Autowired
    private ISocketClientService iSocketClientService;

    @BeforeAll
    public static void before() throws Exception {
        new Thread(() -> {
            try {
                new NettyServer(7000, 4, "GBK", "response").run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                new NettyServer(7001, 4, "UTF-8", "response").run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                new NettyServer(7002, 6, "UTF-8", "").run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                new NettyServer(7003, 0, "UTF-8", "response").run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                new NettyServerError(7004, 6, "GBK", "").run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.SECONDS.sleep(8);
        Assertions.assertFalse(() -> NetUtil.isUsableLocalPort(7000), "端口怎没还没骑起来啊");
        Assertions.assertFalse(() -> NetUtil.isUsableLocalPort(7001), "端口怎没还没骑起来啊");
        Assertions.assertFalse(() -> NetUtil.isUsableLocalPort(7002), "端口怎没还没骑起来啊");
        Assertions.assertFalse(() -> NetUtil.isUsableLocalPort(7003), "端口怎没还没骑起来啊");
        Assertions.assertFalse(() -> NetUtil.isUsableLocalPort(7004), "端口怎没还没骑起来啊");
    }

    @Test
    public void client_request_success() throws UnsupportedEncodingException {
        String message = "zhaxshasd新华都需要换dadsuifgtdsauiyofgsdiauygfdshkjfgdskjfygdsiu76ftew";
        // gbk编解码
        String s = iSocketClientService.clientRequest("127.0.0.1", 7000, "qps-test-65595210", 3,
                false, message, 4, "GBK");
        Assertions.assertArrayEquals(("response" + message).getBytes("GBK"), s.getBytes("GBK"));

        // utf8编解码
        s = iSocketClientService.clientRequest("127.0.0.1", 7001, "qps-test-65595210", 3,
                false, message, 4, "UTF-8");
        Assertions.assertArrayEquals(("response" + message).getBytes(StandardCharsets.UTF_8), s.getBytes(StandardCharsets.UTF_8));

        // utf8编解码，发送空内容，响应空内容
        s = iSocketClientService.clientRequest("127.0.0.1", 7002, "qps-test-65595210", 3,
                false, "", 6, "UTF-8");
        Assertions.assertNull(s);

        // utf8编解码，长度域为0
        s = iSocketClientService.clientRequest("127.0.0.1", 7003, "qps-test-65595210", 3,
                false, message, 0, "UTF-8");
        Assertions.assertArrayEquals(("response" + message).getBytes(StandardCharsets.UTF_8), s.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void client_request_length_error() throws UnsupportedEncodingException {
        String message = "zhaxshasd新华都需要换dadsuifgtdsauiyofgsdiauygfdshkjfgdskjfygdsiu76ftew";
        // 客户端长度域比服务端长
        String s = null;
        try {
            s = iSocketClientService.clientRequest("127.0.0.1", 7001, "qps-test-65595210", 3,
                    false, message, 6, "UTF-8");
        } catch (Exception e) {
            Assertions.assertNotNull(e);
            Assertions.assertNull(s);
        }

        // 客户端长度域比服务端短
        try {
            s = iSocketClientService.clientRequest("127.0.0.1", 7001, "qps-test-65595210", 3,
                    false, message, 3, "UTF-8");
        } catch (Exception e) {
            Assertions.assertNotNull(e);
            Assertions.assertNull(s);
        }
    }

    @Test
    public void client_response_length_error() throws UnsupportedEncodingException {
        String message = "zhaxshasd新华都需要换dadsuifgtdsauiyofgsdiauygfdshkjfgdskjfygdsiu76ftew";
        // 响应长度域的内容为n个0
        String s = null;
        try {
            s = iSocketClientService.clientRequest("127.0.0.1", 7004, "qps-test-65595210", 3,
                    false, message, 6, "UTF-8");
        } catch (Exception e) {
            Assertions.assertNotNull(e);
            Assertions.assertNull(s);
        }
    }

    @Test
    public void test_91093() throws UnsupportedEncodingException {
        String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\">\n" +
                "  <SERVICE_HEADER>\n" +
                "    <SERVICE_SN>2023041200000000039</SERVICE_SN>\n" +
                "    <SERVICE_ID>91093</SERVICE_ID>\n" +
                "    <ORG>000064836560</ORG>\n" +
                "    <CHANNEL_ID>08</CHANNEL_ID>\n" +
                "    <OP_ID>8802104</OP_ID>\n" +
                "    <REQUST_TIME>20230412102233</REQUST_TIME>\n" +
                "    <VERSION_ID>01</VERSION_ID>\n" +
                "    <MAC>EEEEEEEEEEEEEE</MAC>\n" +
                "  </SERVICE_HEADER>\n" +
                "  <SERVICE_BODY>\n" +
                "    <EXT_ATTRIBUTES>\n" +
                "      <AUTH/>\n" +
                "    </EXT_ATTRIBUTES>\n" +
                "    <REQUEST>\n" +
                "      <OPT>0</OPT>\n" +
                "      <SGN_NO>Z000000000009020230412639831123809</SGN_NO>\n" +
                "      <SGN_ORG_ID>C1438151000010</SGN_ORG_ID>\n" +
                "      <SGN_ACCT_TP>01</SGN_ACCT_TP>\n" +
                "      <SGN_ACCT_ID>6230480008990086398</SGN_ACCT_ID>\n" +
                "      <SGN_ACCT_NM>尚夜白</SGN_ACCT_NM>\n" +
                "      <ID_TP>01</ID_TP>\n" +
                "      <ID_NO>520402199210030943</ID_NO>\n" +
                "      <MOB_NO>18240571795</MOB_NO>\n" +
                "    </REQUEST>\n" +
                "  </SERVICE_BODY>\n" +
                "</SERVICE>\n";
        // 响应长度域的内容为n个0
        StopWatch stopWatch = new StopWatch();
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            stopWatch.start();
            String s = iSocketClientService.clientRequest("10.250.28.142", 7000, "qps-test-65595210", 3,
                    false, message, 6, "UTF-8");
            stopWatch.stop();
        });
//        System.out.println(s);
        System.out.println(stopWatch.prettyPrint());
    }

    @Test
    public void test_16050() {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SERVICE xmlns=\"http://www.allinfinance.com/dataspec/\"><SERVICE_HEADER><SERVICE_SN>20230403172423776452</SERVICE_SN><SERVICE_ID>16050</SERVICE_ID><ORG>000064020000</ORG><CHANNEL_ID>06</CHANNEL_ID><REQUST_TIME>20230403172423</REQUST_TIME><VERSION_ID>01</VERSION_ID></SERVICE_HEADER><SERVICE_BODY><REQUEST><CARD_NO>6251931866000028</CARD_NO><ID_TYPE>W</ID_TYPE><ID_NO>20200101</ID_NO><MOBILE_NO>15687089611</MOBILE_NO><CUST_NAME>蔡英文</CUST_NAME></REQUEST></SERVICE_BODY></SERVICE>";
        String response = iSocketClientService.clientRequest("10.250.20.56", 13003, "qps-test-65595210", 3,
                false, request, 6, "UTF-8");
        System.out.println(response);
    }
}