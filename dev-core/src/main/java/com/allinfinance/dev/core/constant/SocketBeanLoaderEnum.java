package com.allinfinance.dev.core.constant;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 张勇
 * @date 2020-11-28 01:25
 */
public enum SocketBeanLoaderEnum {
    /* 应用名 */
    DEV_SOCKKET_APP_NAME("dev.socket.appName", "dev01"),
    /* 应用端口，默认4399 */
    DEV_SOCKET_PORT("dev.socket.port", "0"),
    /* 核心处理线程数 */
    DEV_SOCKET_PROC_COUNT("dev.socket.processCount", "10"),
    /* 线程池大小 */
    DEV_SOCKET_THREAD_COUNT("dev.socket.threadCount", "50"),
    /* 请求报文头中长度位数 */
    DEV_SOCKET_DEC_LENGTH("dev.socket.decode.length", "6"),
    /* 响应报文头中长度位数 */
    DEV_SOCKET_ENC_LENGTH("dev.socket.encode.length", "6"),
    /* 请求报文字符集 */
    DEV_SOCKET_DEC_CHARSET("dev.socket.decode.charset", "UTF-8"),
    /* 响应报文字符集 */
    DEV_SOCKET_ENC_CHARSET("dev.socket.encode.charset", "UTF-8"),
    /* 报文缓冲区大小 */
    DEV_SOCKET_BUFFER_SIZE("dev.socket.bufferSize", "8096"),
    /* 处理超时时间 */
    DEV_SOCKET_TIMEOUT("dev.socket.timeout", "10"),
    /* 请求处理类类名 */
    DEV_SOCKET_HANDLER("dev.socket.handler", ""),
    /* 请求处理类类名 */
    DEV_SOCKET_DECODER("dev.socket.decoder", "com.allinfinance.dev.core.util.socket.codec.DemuxingMessageDecoder"),
    /* 请求处理类类名 */
    DEV_SOCKET_ENCODER("dev.socket.encoder", "com.allinfinance.dev.core.util.socket.codec.DemuxingMessageEncoder"),
    /* TIME_WAIT生效开关 */
    DEV_SOCKET_SOLINGER("dev.socket.soLinger", "false");

    private final String keyName;
    private final String defaultValue;

    SocketBeanLoaderEnum(String keyName, String defaultValue) {
        this.keyName = keyName;
        this.defaultValue = defaultValue;
    }


    public String getKeyName() {
        return keyName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * 遍历当前枚举的值列表，获取配置文件对应字段值，若为空，则返回该字段默认值
     *
     * @return java.util.Map<com.allinfinance.dev.core.constant.SocketBeanLoderEnum, java.lang.String>
     * @author 张勇
     * @date 2020-11-28 01:32
     * @params properties
     */
    public static Map<SocketBeanLoaderEnum, String> getPropertyValue(PropertiesConfiguration properties) {
        Map<SocketBeanLoaderEnum, String> propertyValueMap = new LinkedHashMap<>();
        for (SocketBeanLoaderEnum socketBeanLoaderEnum : SocketBeanLoaderEnum.values()) {
            propertyValueMap.put(socketBeanLoaderEnum,
                    StringUtils.isBlank(properties.getString(socketBeanLoaderEnum.getKeyName())) ?
                            socketBeanLoaderEnum.getDefaultValue() : properties.getString(socketBeanLoaderEnum.getKeyName()));
        }
        return propertyValueMap;
    }
}
