package com.allinfinance.dev.hsp.socket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Classname  com.allinfinance.dev.hsp.socket.HsmSocketTest
 *
 * @Description TODO
 * @Date 2021/3/24 17:50
 * @Created by ZhangYong
 */
@Slf4j
public class HsmSocketTest {
    private static final String host = "10.250.5.150";
    private static Integer count = 0;

    public byte[] request(byte[] request) throws IOException {
        count++;
        Socket socket = new Socket(host, 6666);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        outputStream.write(request);
        outputStream.flush();

        byte[] resp = new byte[1024];
        inputStream.read(resp);

        outputStream.close();
        inputStream.close();
        socket.close();
        log.debug("调用加密机次数:{}", count);
        return resp;

    }
}
