package com.allinfinance.dev.socket.server.scaffold.configure;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-09-22 13:51
 */
public class ServerMetadataConfigure {
    /**
     * 服务端名称
     */
    private String name;
    /**
     * 服务端端口
     */
    private String port;
    /**
     * 心跳检测读超时时间
     */
    private String readerIdleTime = "60000";
    /**
     * 心跳检测写超时时间
     */
    private String writerIdleTime = "60000";
    /**
     * 解码报文头长度：4 、 6
     */
    private String decodeMsgLength = "6";
    /**
     * 编码报文头长度：4 、 6
     */
    private String encodeMsgLength = "6";
    /**
     * 服务端解码格式 ：UTF-8
     */
    private String decodeCharset = "UTF-8";
    /**
     * 服务端编码格式 ：UTF-8
     */
    private String encodeCharset = "UTF-8";
    /**
     * 具体业务处理器
     */
    private String handlerClassName;
    /**
     * 解码器
     */
    private String decoderClassName;
    /**
     * 编码器
     */
    private String encoderClassName;
    /**
     * 服务端驱动：netty
     */
    private String serverDriver = "netty";
    /**
     * 服务端底层实现类：default
     */
    private String bootstrap = "default";

    public ServerMetadataConfigure() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDecodeMsgLength() {
        return decodeMsgLength;
    }

    public void setDecodeMsgLength(String decodeMsgLength) {
        this.decodeMsgLength = decodeMsgLength;
    }

    public String getEncodeMsgLength() {
        return encodeMsgLength;
    }

    public void setEncodeMsgLength(String encodeMsgLength) {
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

    public String getServerDriver() {
        return serverDriver;
    }

    public void setServerDriver(String serverDriver) {
        this.serverDriver = serverDriver;
    }

    public String getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(String readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    public String getWriterIdleTime() {
        return writerIdleTime;
    }

    public void setWriterIdleTime(String writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }

    public String getBootstrap() {
        return bootstrap;
    }

    public ServerMetadataConfigure setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
        return this;
    }

    @Override
    public String toString() {
        return "ServerMetadataConfigure{" +
                "name='" + name + '\'' +
                ", port='" + port + '\'' +
                ", readerIdleTime='" + readerIdleTime + '\'' +
                ", writerIdleTime='" + writerIdleTime + '\'' +
                ", decodeMsgLength='" + decodeMsgLength + '\'' +
                ", encodeMsgLength='" + encodeMsgLength + '\'' +
                ", decodeCharset='" + decodeCharset + '\'' +
                ", encodeCharset='" + encodeCharset + '\'' +
                ", handlerClassName='" + handlerClassName + '\'' +
                ", decoderClassName='" + decoderClassName + '\'' +
                ", encoderClassName='" + encoderClassName + '\'' +
                ", serverDriver='" + serverDriver + '\'' +
                ", bootstrap='" + bootstrap + '\'' +
                '}';
    }
}
