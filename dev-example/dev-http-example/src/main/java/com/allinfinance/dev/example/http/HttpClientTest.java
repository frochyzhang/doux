package com.allinfinance.dev.example.http;

import com.alibaba.fastjson.JSONObject;
import com.allinfinance.dev.core.util.http.client.HttpsClientTrustService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class HttpClientTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientTest.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-context-test.xml");
        HttpsClientTrustService httpClientService = applicationContext.getBean("httpsClientTrustService", HttpsClientTrustService.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("appId", "EUCP-EMY-SMS1-7YAL6");
        header.put("gzip", "on");
        header.put("encode", "UTF-8");
        String url = "https://36.110.217.75:443/inter/getReport";

        EmayGetRequest request = new EmayGetRequest();
        request.setNumber(10);
        request.setRequestTime(System.currentTimeMillis() + "");
        request.setRequestValidPeriod("5");
        byte[] bytes = JSONObject.toJSONString(request).getBytes(StandardCharsets.UTF_8);
        //压缩
        bytes = compress(bytes);
        //加密
        byte[] msg = encrypt(bytes, "48C5C7E73B30035B".getBytes(), "AES/ECB/PKCS5Padding");

        HashMap<String, Object> map = httpClientService.httpRequest(header, msg, url, 0, 0);
        byte[] body = (byte[]) map.get("body");
        byte[] decryptMsg = decrypt(body, "48C5C7E73B30035B".getBytes(), "AES/ECB/PKCS5Padding");
        if (decryptMsg == null || decryptMsg.length == 0) {
            logger.info("decryptMsg is null");
        }
        logger.info("decryptMsg:{}", Arrays.toString(decryptMsg));
        byte[] decompressMsg = decompress(decryptMsg);
        if (decompressMsg.length == 0) {
            logger.info("decompressMsg is null");
        }
        logger.info("decompressMsg:{}", Arrays.toString(decompressMsg));
        String respStr = new String(decompressMsg, StandardCharsets.UTF_8);
        logger.info("respStr:" + respStr);
    }

    public static byte[] compress(byte[] bytes) throws IOException {
        ByteArrayOutputStream out = null;
        GZIPOutputStream gos = null;
        try {
            out = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(out);
            gos.write(bytes);
            gos.finish();
            gos.flush();
        } finally {
            if (gos != null) {
                gos.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return out.toByteArray();
    }

    public static byte[] decompress(byte[] bytes) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream gin = new GZIPInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int count;
        byte[] data = new byte[1024];
        while ((count = gin.read(data, 0, 1024)) != -1) {
            out.write(data, 0, count);
        }
        out.flush();
        out.close();
        gin.close();
        return out.toByteArray();
    }

    public static byte[] encrypt(byte[] content, byte[] password, String algorithm) {
        if (content == null || password == null) {
            return null;
        }
        try {
            Cipher cipher;
            if (algorithm.endsWith("PKCS7Padding")) {
                cipher = Cipher.getInstance(algorithm, "BC");
            } else {
                cipher = Cipher.getInstance(algorithm);
            }
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(password, "Aes"));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(byte[] content, byte[] password, String algorithm) {
        if (content == null || password == null) {
            logger.info("input null");
            return null;
        }
        try {
            Cipher cipher;
            if (algorithm.endsWith("PKCS7Padding")) {
                cipher = Cipher.getInstance(algorithm, "BC");
            } else {
                cipher = Cipher.getInstance(algorithm);
            }
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(password, "Aes"));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static class EmayGetRequest {
        //每次获取状态报告数量
        private int number;
        //请求时间(时间毫秒值)
        private String requestTime;
        //请求有效时间(秒)
        private String requestValidPeriod;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getRequestValidPeriod() {
            return requestValidPeriod;
        }

        public void setRequestValidPeriod(String requestValidPeriod) {
            this.requestValidPeriod = requestValidPeriod;
        }

        @Override
        public String toString() {
            return "EmayGetRequest{" +
                    "number=" + number +
                    ", requestTime='" + requestTime + '\'' +
                    ", requestValidPeriod='" + requestValidPeriod + '\'' +
                    '}';
        }
    }
}
