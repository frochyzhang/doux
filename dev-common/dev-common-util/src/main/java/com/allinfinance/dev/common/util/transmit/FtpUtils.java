package com.allinfinance.dev.common.util.transmit;

import org.apache.camel.catalog.CamelCatalog;
import org.apache.camel.catalog.DefaultCamelCatalog;

import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * @author qipeng
 * @date 2022/9/13 11:38
 * @desc ftp上传下载工具类
 */
public class FtpUtils {

    public static void main(String[] args) throws URISyntaxException {
        CamelCatalog catalog = new DefaultCamelCatalog();
        System.out.println(catalog.componentJSonSchema("ftp"));

        HashMap<String, String> properties = new HashMap<>();
        properties.put("host", "10.250.20.207");

        properties.put("port", "22");
        properties.put("username", "wkftp");
        properties.put("password", "wkftp");

        properties.put("filename", "NUCC_SIGN_000064572600_20211223");
        properties.put("directoryName", "/");


        String uri = catalog.asEndpointUri("ftp", properties, true);
        System.out.println(uri);

    }
}
