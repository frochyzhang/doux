package com.allinfinance.dev.core.bean;

/**
 * @author 张勇
 * @date 2020-11-28 01:25
 */
@Deprecated
public class MinaSocketBean {

    private String name;
    private Integer port;
    private Integer processorCount;
    private Integer threadCount;
    private Integer decodeMsgLength;
    private Integer encodeMsgLength;
    private String decodeCharset;
    private String encodeCharset;
    private Integer bufferSize;
    private Integer timeOut;
    private String handlerClassName;
    private String decoderClassName;
    private String encoderClassName;
    private Boolean soLinger;

    //长链接相关参数
    private Integer beatTimeout;
    private Integer beatInterval;
    private Boolean keepAlive;

    public MinaSocketBean() {
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

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
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

    public Boolean getSoLinger() {
        return soLinger;
    }

    public void setSoLinger(Boolean soLinger) {
        this.soLinger = soLinger;
    }

    public Integer getBeatTimeout() {
        return beatTimeout;
    }

    public void setBeatTimeout(Integer beatTimeout) {
        this.beatTimeout = beatTimeout;
    }

    public Integer getBeatInterval() {
        return beatInterval;
    }

    public void setBeatInterval(Integer beatInterval) {
        this.beatInterval = beatInterval;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    @Override
    public String toString() {
        return "MinaSocketBean{" +
                "name='" + name + '\'' +
                ", port=" + port +
                ", processorCount=" + processorCount +
                ", threadCount=" + threadCount +
                ", decodeMsgLength=" + decodeMsgLength +
                ", encodeMsgLength=" + encodeMsgLength +
                ", decodeCharset='" + decodeCharset + '\'' +
                ", encodeCharset='" + encodeCharset + '\'' +
                ", bufferSize=" + bufferSize +
                ", timeOut=" + timeOut +
                ", handlerClassName='" + handlerClassName + '\'' +
                ", decoderClassName='" + decoderClassName + '\'' +
                ", encoderClassName='" + encoderClassName + '\'' +
                ", soLinger=" + soLinger +
                ", beatTimeout='" + beatTimeout + '\'' +
                ", beatInterval='" + beatInterval + '\'' +
                ", keepAlive=" + keepAlive +
                '}';
    }
}
