package com.allinfinance.dev.core.bean;

import com.allinfinance.dev.core.constant.SocketBeanLoaderEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author 张勇
 * @date 2020-11-28 01:25
 */
public class MinaSocketBean {

    private String name;
    private Integer port;
    private Integer processorCount;
    private Integer decodeMsgLength;
    private Integer encodeMsgLength;
    private String decodeCharset;
    private String encodeCharset;
    private Integer bufferSize;
    private Integer timeOut;
    private String handlerClassName;
    private String decoderClassName;
    private String encoderClassName;

    public MinaSocketBean(Map<SocketBeanLoaderEnum, String> propertyValueMap) throws Exception {
        this.name = propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKKET_APP_NAME);
        this.port = Integer.valueOf(propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_PORT));
        this.processorCount = Integer.valueOf(propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_PROC_COUNT));
        this.decodeMsgLength = Integer.valueOf(propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_DEC_LENGTH));
        this.encodeMsgLength = Integer.valueOf(propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_ENC_LENGTH));
        this.decodeCharset = propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_DEC_CHARSET);
        this.encodeCharset = propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_ENC_CHARSET);
        this.bufferSize = Integer.valueOf(propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_BUFFER_SIZE));
        this.timeOut = Integer.valueOf(propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_TIMEOUT));
        this.handlerClassName = propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_HANDLER);
        this.decoderClassName = propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_DECODER);
        this.encoderClassName = propertyValueMap.get(SocketBeanLoaderEnum.DEV_SOCKET_ENCODER);
        if (this.port == 0 || StringUtils.isBlank(this.handlerClassName)) {
            throw new Exception("运行端口及报文路径类全路径未指定!");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getProcessorCount() {
        return processorCount;
    }

    public void setProcessorCount(Integer processorCount) {
        this.processorCount = processorCount;
    }

    public Integer getDecodeMsgLength() {
        return decodeMsgLength;
    }

    public void setDecodeMsgLength(Integer decodeMsgLength) {
        this.decodeMsgLength = decodeMsgLength;
    }

    public Integer getEncodeMsgLength() {
        return encodeMsgLength;
    }

    public void setEncodeMsgLength(Integer encodeMsgLength) {
        this.encodeMsgLength = encodeMsgLength;
    }

    public String getDecodeCharset() {
        return decodeCharset;
    }

    public void setDecodeCharset(String decodeCharset) {
        this.decodeCharset = decodeCharset;
    }

    public String getEncodeCharset() {
        return encodeCharset;
    }

    public void setEncodeCharset(String encodeCharset) {
        this.encodeCharset = encodeCharset;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public String getHandlerClassName() {
        return handlerClassName;
    }

    public void setHandlerClassName(String handlerClassName) {
        this.handlerClassName = handlerClassName;
    }

    public String getDecoderClassName() {
        return decoderClassName;
    }

    public void setDecoderClassName(String decoderClassName) {
        this.decoderClassName = decoderClassName;
    }

    public String getEncoderClassName() {
        return encoderClassName;
    }

    public void setEncoderClassName(String encoderClassName) {
        this.encoderClassName = encoderClassName;
    }

    @Override
    public String toString() {
        return "MinaSocketBean{" +
                "port=" + port +
                ", processorCount=" + processorCount +
                ", decodeMsgLength=" + decodeMsgLength +
                ", encodeMsgLength=" + encodeMsgLength +
                ", decodeCharset='" + decodeCharset + '\'' +
                ", encodeCharset='" + encodeCharset + '\'' +
                ", bufferSize=" + bufferSize +
                ", timeOut=" + timeOut +
                '}';
    }
}
