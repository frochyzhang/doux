package com.allinfinance.dev.ccp.service;

import cn.hutool.core.net.NetUtil;
import com.allinfinance.dev.common.socket.api.client.SocketClientService;
import com.allinfinance.dev.common.socket.api.client.dto.SocketRequestDTO;
import com.allinfinance.dev.common.socket.api.client.dto.SocketResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SocketClientServiceImplTest {
    @Autowired
    private SocketClientService socketService;

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
//        // gbk编解码
//        String s = iSocketClientService.clientRequest("127.0.0.1", 7000, "qps-test-65595210", 3,
//                false, message, 4, "GBK");
//        Assertions.assertArrayEquals(("response" + message).getBytes("GBK"), s.getBytes("GBK"));
//
//        // utf8编解码
//        s = iSocketClientService.clientRequest("127.0.0.1", 7001, "qps-test-65595210", 3,
//                false, message, 4, "UTF-8");
//        Assertions.assertArrayEquals(("response" + message).getBytes(StandardCharsets.UTF_8), s.getBytes(StandardCharsets.UTF_8));
//
//        // utf8编解码，发送空内容，响应空内容
//        s = iSocketClientService.clientRequest("127.0.0.1", 7002, "qps-test-65595210", 3,
//                false, "", 6, "UTF-8");
//        Assertions.assertNull(s);

        // utf8编解码，长度域为0
        SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7003", "qps-test-65595210", "0", "UTF-8");
        SocketResponseDTO socketResponseDTO = socketService.request(socketRequestDTO, message);
        Assertions.assertTrue(socketResponseDTO.getSuccess(), "socketService返回失败");
        String s = socketResponseDTO.getResponse();
        Assertions.assertArrayEquals(("response" + message).getBytes(StandardCharsets.UTF_8), s.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void client_request_length_error() throws UnsupportedEncodingException {
        String message = "zhaxshasd新华都需要换dadsuifgtdsauiyofgsdiauygfdshkjfgdskjfygdsiu76ftew";
        // 客户端长度域比服务端长
        String s = null;
        try {
            SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7001", "qps-test-65595210", "6", "UTF-8");
            SocketResponseDTO socketResponseDTO = socketService.request(socketRequestDTO, message);
            Assertions.assertTrue(socketResponseDTO.getSuccess(), "socketService返回失败");
            s = socketResponseDTO.getResponse();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
            Assertions.assertNull(s);
        }

        // 客户端长度域比服务端短
        try {
            SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7001", "qps-test-65595210", "3", "UTF-8");
            SocketResponseDTO socketResponseDTO = socketService.request(socketRequestDTO, message);
            Assertions.assertTrue(socketResponseDTO.getSuccess(), "socketService返回失败");
            s = socketResponseDTO.getResponse();
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
            SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7004", "qps-test-65595210", "6", "UTF-8");
            SocketResponseDTO socketResponseDTO = socketService.request(socketRequestDTO, message);
            Assertions.assertTrue(socketResponseDTO.getSuccess(), "socketService返回失败");
            s = socketResponseDTO.getResponse();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
            Assertions.assertNull(s);
        }
    }
}