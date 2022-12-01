package com.allinfinance.dev.rpc.scaffold.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
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
     * 引用公共服务列表，以数组形式提供
     */
    private List<String> commonServiceList;
    /**
     * provider的uniqueId
     */
    private String providerUniqueId;
    /**
     * 服务提供方的包路径
     */
    private String providerPackage;
    /**
     * RPC客户端配置
     */
    private Consumer consumer;

    private Bootstrap bootstrap = new Bootstrap();

    public List<String> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<String> referenceList) {
        this.referenceList = referenceList;
    }

    public List<String> getCommonServiceList() {
        return commonServiceList;
    }

    public void setCommonServiceList(List<String> commonServiceList) {
        this.commonServiceList = commonServiceList;
    }

    public String getProviderUniqueId() {
        return providerUniqueId;
    }

    public void setProviderUniqueId(String providerUniqueId) {
        this.providerUniqueId = providerUniqueId;
    }

    public String getProviderPackage() {
        return providerPackage;
    }

    public void setProviderPackage(String providerPackage) {
        this.providerPackage = providerPackage;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public static class Consumer {
        /**
         * 客户端调用超时时间
         */
        private Integer timeout;
        /**
         * 客户端调用类型，默认为同步，设置为future时为异步调用
         */
        private String invokeType;

        public Integer getTimeout() {
            return timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }

        public String getInvokeType() {
            return invokeType;
        }

        public void setInvokeType(String invokeType) {
            this.invokeType = invokeType;
        }

        @Override
        public String toString() {
            return "Consumer{" +
                    "timeout=" + timeout +
                    ", future=" + invokeType +
                    '}';
        }
    }

    public static class Bootstrap implements Serializable {

        public static final String BOOT_ENABLE = "com.allinfinance.rpc.bootstrap.enable";

        public static final String REGISTRY_PROTOCOL = "nacos";

        public static final String TRANSPORT_PROTOCOL = "bolt";
        /**
         * 是否注册到网关
         */
        private Boolean enable;
        /**
         * exporter端口
         */
        private Integer exporterPort;
        /**
         * TCP网关注册中心地址
         */
        private String gateRegistry;
        /**
         * 应用唯一标识
         */
        private String appUniqueId;
        /**
         * ProcessService扩展实现
         */
        private String processServiceExtension = "default";
        /**
         * 集群分组标识
         */
        private String groupId;
        /**
         * 网关集群地址
         */
        private String gateClusterAddress;
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

        public Integer getExporterPort() {
            return exporterPort;
        }

        public void setExporterPort(Integer exporterPort) {
            this.exporterPort = exporterPort;
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

        public String getProcessServiceExtension() {
            return processServiceExtension;
        }

        public void setProcessServiceExtension(String processServiceExtension) {
            this.processServiceExtension = processServiceExtension;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGateClusterAddress() {
            return gateClusterAddress;
        }

        public void setGateClusterAddress(String gateClusterAddress) {
            this.gateClusterAddress = gateClusterAddress;
        }

        public List<AppConfigList> getAppList() {
            return appList;
        }

        public void setAppList(List<AppConfigList> appList) {
            this.appList = appList;
        }

        public static class AppConfigList implements Serializable {
            private Type type;
            private String appDesc;
            private Integer listenPort;
            private TcpConfig tcpConfig = new TcpConfig();
            private HttpConfig httpConfig = new HttpConfig();

            public enum Type implements Serializable {
                TCP, HTTP;

            }

            public static class TcpConfig implements Serializable {
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
                private String handlerClassName = "com.allinfinance.dev.gateway.util.DefaultTcpIOHandler";
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

            public static class HttpConfig implements Serializable {
                /**
                 * 是否开启TCP_NODELAY
                 */
                private Boolean tcpNoDelay = true;
                /**
                 * 是否开启TCP_REUSEADDR
                 */
                private Boolean soReUseAddr = true;
                /**
                 * 是否开启SO_KEEPALIVE
                 */
                private Boolean soKeepAlive = false;
                /**
                 * 请求缓冲池大小
                 */
                private Integer soRcvBuf = 2048;
                /**
                 * 响应缓冲池大小
                 */
                private Integer soSndBuf = 2048;
                /**
                 * 处理的URL列表
                 */
                private List<UrlConfig> urlList;

                public static class UrlConfig implements Serializable {
                    /**
                     * URL
                     */
                    private String url;
                    /**
                     * 请求类型
                     */
                    private HttpMethod requestMethod;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public HttpMethod getRequestMethod() {
                        return requestMethod;
                    }

                    public void setRequestMethod(HttpMethod requestMethod) {
                        this.requestMethod = requestMethod;
                    }

                    @Override
                    public String toString() {
                        return "UrlConfig{" +
                                "url='" + url + '\'' +
                                ", requestMethod=" + requestMethod +
                                '}';
                    }
                }

                public Boolean getTcpNoDelay() {
                    return tcpNoDelay;
                }

                public void setTcpNoDelay(Boolean tcpNoDelay) {
                    this.tcpNoDelay = tcpNoDelay;
                }

                public Boolean getSoReUseAddr() {
                    return soReUseAddr;
                }

                public void setSoReUseAddr(Boolean soReUseAddr) {
                    this.soReUseAddr = soReUseAddr;
                }

                public Boolean getSoKeepAlive() {
                    return soKeepAlive;
                }

                public void setSoKeepAlive(Boolean soKeepAlive) {
                    this.soKeepAlive = soKeepAlive;
                }

                public Integer getSoRcvBuf() {
                    return soRcvBuf;
                }

                public void setSoRcvBuf(Integer soRcvBuf) {
                    this.soRcvBuf = soRcvBuf;
                }

                public Integer getSoSndBuf() {
                    return soSndBuf;
                }

                public void setSoSndBuf(Integer soSndBuf) {
                    this.soSndBuf = soSndBuf;
                }

                public List<UrlConfig> getUrlList() {
                    return urlList;
                }

                public void setUrlList(List<UrlConfig> urlList) {
                    this.urlList = urlList;
                }

                @Override
                public String toString() {
                    return "HttpConfig{" +
                            "tcpNoDelay=" + tcpNoDelay +
                            ", soReUseAddr=" + soReUseAddr +
                            ", soKeepAlive=" + soKeepAlive +
                            ", soRcvBuf=" + soRcvBuf +
                            ", soSndBuf=" + soSndBuf +
                            ", urlList=" + urlList +
                            '}';
                }
            }

            public Type getType() {
                return type;
            }

            public void setType(Type type) {
                this.type = type;
            }

            public String getAppDesc() {
                return appDesc;
            }

            public void setAppDesc(String appDesc) {
                this.appDesc = appDesc;
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
                        ", appDesc='" + appDesc + '\'' +
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
                    ", exporterPort=" + exporterPort +
                    ", gateRegistry='" + gateRegistry + '\'' +
                    ", appUniqueId='" + appUniqueId + '\'' +
                    ", processServiceExtension='" + processServiceExtension + '\'' +
                    ", groupId='" + groupId + '\'' +
                    ", gateClusterAddress='" + gateClusterAddress + '\'' +
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
