package com.allinfinance.dev.ccp;

import cn.hutool.core.net.NetUtil;
import com.allinfinance.dev.ccp.server.NettyServer;
import com.allinfinance.dev.common.socket.api.client.SocketClientService;
import com.allinfinance.dev.common.socket.api.client.dto.SocketRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2023-04-07 10:28
 */
@SpringBootTest
public class SocketClientServiceImplTest {
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

        TimeUnit.SECONDS.sleep(8);
        Assertions.assertFalse(() -> NetUtil.isUsableLocalPort(7000), "端口未启动");
        Assertions.assertFalse(() -> NetUtil.isUsableLocalPort(7001), "端口未启动");
        Assertions.assertFalse(() -> NetUtil.isUsableLocalPort(7002), "端口未启动");
        Assertions.assertFalse(() -> NetUtil.isUsableLocalPort(7003), "端口未启动");
    }

    @Test
    public void client_request_success() throws UnsupportedEncodingException {
        String message = "";
        String s = null;
        // utf8编解码，发送空内容，响应空内容
        SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7002", "ccp", "6", "UTF-8");
        s = socketService.request(socketRequestDTO, message).getResponse();
        Assertions.assertNull(s);
        // utf8编解码，长度域为0
        message = "p2165164165416514646541656";
        socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7003", "ccp", "0", "UTF-8");
        s = socketService.request(socketRequestDTO, message).getResponse();
        Assertions.assertArrayEquals(("response" + message).getBytes(StandardCharsets.UTF_8), s.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void client_request_other_msgEncode() throws UnsupportedEncodingException {
        String message = "p2165164165416514646541656";
        String s = null;
        //gbk编解码
        SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7000", "ccp", "4", "GBK");
        s = socketService.request(socketRequestDTO, message).getResponse();
        Assertions.assertArrayEquals(("response" + message).getBytes("GBK"), s.getBytes("GBK"));
        // utf8编解码
        socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7001", "ccp", "4", "UTF-8");
        s = socketService.request(socketRequestDTO, message).getResponse();
        Assertions.assertArrayEquals(("response" + message).getBytes(StandardCharsets.UTF_8), s.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void client_request_length_error() throws UnsupportedEncodingException {
        String message = "P2165164165416514646541656";
        // 客户端(5)长度域比服务端(4)长
        String s = null;
        try {
            SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7001", "ccp", "5", "UTF-8");
            s = socketService.request(socketRequestDTO, message).getResponse();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
            Assertions.assertNull(s);
        }
        // 客户端(3)长度域比服务端(4)短
        try {
            SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7001", "ccp", "3", "UTF-8");
            s = socketService.request(socketRequestDTO, message).getResponse();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
            Assertions.assertNull(s);
        }
        //客户端(3)长度域比服务端(4)短，且报文内容为数字导致服务端读取报文长度过长->循环读直到客户端超时
        String msg = "2165164165416514646541656";
        String ss = null;
        try {
            SocketRequestDTO socketRequestDTO = new SocketRequestDTO("127.0.0.1", "7001", "ccp", "3", "UTF-8");
            ss = socketService.request(socketRequestDTO, msg).getResponse();
        } catch (Exception e) {
            Assertions.assertNotNull(e);
            Assertions.assertNull(ss);
        }
    }
}
