package com.allinfinance.dev.rpc.scaffold.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author qipeng
 * @date 2021/12/30 15:39
 */
@ConfigurationProperties(prefix = "com.allinfinance.rpc")
public class RpcConfigurationProperties {
    /**
     * 引用接口列表，以数组形式提供
     */
    private List<String> referenceList;
    /**
     * 服务提供方的包路径
     */
    private String providerPackage;

    private Bootstrap bootstrap = new Bootstrap();

    public String getProviderPackage() {
        return providerPackage;
    }

    public void setProviderPackage(String providerPackage) {
        this.providerPackage = providerPackage;
    }

    public List<String> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<String> referenceList) {
        this.referenceList = referenceList;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public static class Bootstrap {

        public static final String BOOT_ENABLE = "com.allinfinance.rpc.bootstrap.enable";

        public static final String REGISTRY_PROTOCOL = "nacos";

        public static final String TRANSPORT_PROTOCOL = "bolt";
        /**
         * 是否注册到网关
         */
        private Boolean enable;
        /**
         * TCP网关注册中心地址
         */
        private String gateRegistry;
        /**
         * 应用唯一标识
         */
        private String appUniqueId;
        /**
         * 注册应用详情
         */
        private List<AppConfigList> appList = new ArrayList<>();

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public String getGateRegistry() {
            return gateRegistry;
        }

        public void setGateRegistry(String gateRegistry) {
            this.gateRegistry = gateRegistry;
        }

        public String getAppUniqueId() {
            return appUniqueId;
        }

        public void setAppUniqueId(String appUniqueId) {
            this.appUniqueId = appUniqueId;
        }

        public List<AppConfigList> getAppList() {
            return appList;
        }

        public void setAppList(List<AppConfigList> appList) {
            this.appList = appList;
        }

        public static class AppConfigList {
            private Type type;
            private Integer listenPort;
            private TcpConfig tcpConfig = new TcpConfig();
            private HttpConfig httpConfig = new HttpConfig();

            public enum Type {
                TCP, HTTP;

            }

            ;

            public static class TcpConfig {
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
                private String handlerClassName = "com.allinfinance.dev.example.socket.TestIOHandler";
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

                @Override
                public String toString() {
                    return "TcpConfig{" +
                            "processorCount=" + processorCount +
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
                            '}';
                }
            }

            private static class HttpConfig {
                //TODO
            }

            public Type getType() {
                return type;
            }

            public void setType(Type type) {
                this.type = type;
            }

            public Integer getListenPort() {
                return listenPort;
            }

            public void setListenPort(Integer listenPort) {
                this.listenPort = listenPort;
            }

            public TcpConfig getTcpConfig() {
                return tcpConfig;
            }

            public void setTcpConfig(TcpConfig tcpConfig) {
                this.tcpConfig = tcpConfig;
            }

            public HttpConfig getHttpConfig() {
                return httpConfig;
            }

            public void setHttpConfig(HttpConfig httpConfig) {
                this.httpConfig = httpConfig;
            }

            @Override
            public String toString() {
                return "AppConfigList{" +
                        "type=" + type +
                        ", listenPort=" + listenPort +
                        ", tcpConfig=" + tcpConfig +
                        ", httpConfig=" + httpConfig +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Bootstrap{" +
                    "enable=" + enable +
                    ", gateRegistry='" + gateRegistry + '\'' +
                    ", appUniqueId='" + appUniqueId + '\'' +
                    ", appList=" + appList +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            AtomicReference<Boolean> result = new AtomicReference<>(true);
            Arrays.stream(o.getClass().getDeclaredFields()).forEach(field -> {
                field.setAccessible(true);
                try {
                    Object o1 = field.get(o);
                    Field field1 = this.getClass().getDeclaredField(field.getName());
                    field1.setAccessible(true);
                    Object o2 = field1.get(this);
                    boolean b = o1 != null && o2 != null;
                    if (b && !o1.equals(o2)) {
                        result.set(false);
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    result.set(false);
                }
            });
            return result.get();
        }


    }

}
