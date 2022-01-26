package com.allinfinance.dev.boot.socket.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/1/26 10:32
 */
@ConfigurationProperties("com.allinfinance.dev.socket")
public class SocketConfigProperties {
    /**
     * 监听应用名
     */
    private String name;
    /**
     * 监听端口
     */
    private Integer port;
    /**
     * 请求受理线程数量
     */
    private Integer processorCount = 10;
    /**
     * 业务处理线程数量
     */
    private Integer threadCount = 50;
    /**
     * 接收报文长度
     */
    private Integer decodeMsgLength = 6;
    /**
     * 发出报文长度
     */
    private Integer encodeMsgLength = 6;
    /**
     * 接收报文编码
     */
    private String decodeCharset = "UTF-8";
    /**
     * 发出报文编码
     */
    private String encodeCharset = "UTF-8";
    /**
     * 缓冲池大小
     */
    private Integer bufferSize = 8192;
    /**
     * 超时时间
     */
    private Integer timeOut = 10;
    /**
     * 业务处理类
     */
    private String handlerClassName;
    /**
     * 报文解码处理类
     */
    private String decoderClassName = "com.allinfinance.dev.core.util.socket.codec.DemuxingMessageDecoder";
    /**
     * 报文编码处理类
     */
    private String encoderClassName = "com.allinfinance.dev.core.util.socket.codec.DemuxingMessageEncoder";
    /**
     * 是否开启soLinger开关
     */
    private Boolean soLinger = false;

    @JsonIgnore
    private KeepAliveProperties keepAlive = new KeepAliveProperties(Boolean.FALSE);

    @JsonIgnore
    private List<Config> extConfig = new ArrayList<>();

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

    public KeepAliveProperties getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(KeepAliveProperties keepAlive) {
        this.keepAlive = keepAlive;
    }

    public List<Config> getExtConfig() {
        return extConfig;
    }

    public void setExtConfig(List<Config> extConfig) {
        this.extConfig = extConfig;
    }

    @Override
    public String toString() {
        return "SocketConfigProperties{" +
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
                ", keepAlive=" + keepAlive +
                ", extConfig=" + extConfig +
                '}';
    }

    public static class KeepAliveProperties {
        /**
         * 心跳超时时间（长链接专用）
         */
        private Integer beatTimeout;
        /**
         * 心跳间隔（长链接专用）
         */
        private Integer beatInterval;
        /**
         * 是否开启长链接
         */
        private Boolean enable;

        public KeepAliveProperties(Boolean enable) {
            this.enable = enable;
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

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        @Override
        public String toString() {
            return "KeepAliveProperties{" +
                    "beatTimeout=" + beatTimeout +
                    ", beatInterval=" + beatInterval +
                    ", enable=" + enable +
                    '}';
        }
    }

    public static class Config {
        /**
         * 监听应用名
         */
        private String name;
        /**
         * 监听端口
         */
        private Integer port;
        /**
         * 请求受理线程数量
         */
        private Integer processorCount = 10;
        /**
         * 业务处理线程数量
         */
        private Integer threadCount = 50;
        /**
         * 接收报文长度
         */
        private Integer decodeMsgLength = 6;
        /**
         * 发出报文长度
         */
        private Integer encodeMsgLength = 6;
        /**
         * 接收报文编码
         */
        private String decodeCharset = "UTF-8";
        /**
         * 发出报文编码
         */
        private String encodeCharset = "UTF-8";
        /**
         * 缓冲池大小
         */
        private Integer bufferSize = 8192;
        /**
         * 超时时间
         */
        private Integer timeOut = 10;
        /**
         * 业务处理类
         */
        private String handlerClassName;
        /**
         * 报文解码处理类
         */
        private String decoderClassName = "com.allinfinance.dev.core.util.socket.codec.DemuxingMessageDecoder";
        /**
         * 报文编码处理类
         */
        private String encoderClassName = "com.allinfinance.dev.core.util.socket.codec.DemuxingMessageEncoder";
        /**
         * 是否开启soLinger开关
         */
        private Boolean soLinger = false;

        @JsonIgnore
        private KeepAliveProperties keepAlive = new KeepAliveProperties(Boolean.FALSE);

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

        public KeepAliveProperties getKeepAlive() {
            return keepAlive;
        }

        public void setKeepAlive(KeepAliveProperties keepAlive) {
            this.keepAlive = keepAlive;
        }

        @Override
        public String toString() {
            return "Config{" +
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
                    ", keepAlive=" + keepAlive +
                    '}';
        }
    }
}
