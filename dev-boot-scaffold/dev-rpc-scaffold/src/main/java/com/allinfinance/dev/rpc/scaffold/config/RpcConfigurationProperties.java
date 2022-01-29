package com.allinfinance.dev.rpc.scaffold.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

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

        private Integer listenPort;

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

        public Integer getListenPort() {
            return listenPort;
        }

        public void setListenPort(Integer listenPort) {
            this.listenPort = listenPort;
        }
    }
}
