package com.allinfinance.dev.rpc.scaffold.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Set;

/**
 * @author qipeng
 * @date 2021/12/30 15:39
 */
@ConfigurationProperties(prefix = "com.allinfinance.rpc")
public class RpcConfigurationProperties {
    /**
     * 服务消费者配置
     */
    private Consumer consumer;
    /**
     * 服务提供方配置
     */
    private Provider provider;

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public static class Provider {
        /**
         * 剔除不想对外暴露服务，以数组方式提供
         */
        private Set<String> excludeServiceList;
        /**
         * 服务提供方的包路径
         */
        private String servicePackage;

        public Set<String> getExcludeServiceList() {
            return excludeServiceList;
        }

        public void setExcludeServiceList(Set<String> excludeServiceList) {
            this.excludeServiceList = excludeServiceList;
        }

        public String getServicePackage() {
            return servicePackage;
        }

        public void setServicePackage(String servicePackage) {
            this.servicePackage = servicePackage;
        }

        @Override
        public String toString() {
            return "Provider{" +
                    "excludeServiceList=" + excludeServiceList +
                    ", servicePackage='" + servicePackage + '\'' +
                    '}';
        }
    }

    public static class Consumer {
        /**
         * 引用的公共服务所在的注册中心地址
         */
        private String commonReferenceRegistry;
        /**
         * 引用接口列表，以数组形式提供
         */
        private List<Reference> referenceList;
        /**
         * 引用公共服务列表，以数组形式提供
         */
        private List<Reference> commonReferenceList;

        public String getCommonReferenceRegistry() {
            return commonReferenceRegistry;
        }

        public void setCommonReferenceRegistry(String commonReferenceRegistry) {
            this.commonReferenceRegistry = commonReferenceRegistry;
        }

        public List<Reference> getReferenceList() {
            return referenceList;
        }

        public void setReferenceList(List<Reference> referenceList) {
            this.referenceList = referenceList;
        }

        public List<Reference> getCommonReferenceList() {
            return commonReferenceList;
        }

        public void setCommonReferenceList(List<Reference> commonReferenceList) {
            this.commonReferenceList = commonReferenceList;
        }

        @Override
        public String toString() {
            return "Consumer{" +
                    "commonReferenceRegistry='" + commonReferenceRegistry + '\'' +
                    ", referenceList=" + referenceList +
                    ", commonReferenceList=" + commonReferenceList +
                    '}';
        }

        public static class Reference {
            /**
             * 消费端调用超时时间，默认3s
             */
            private Integer timeout = 3000;
            /**
             * 客户端调用类型，默认为同步，设置为future时为异步调用
             */
            private String invokeType = "sync";
            /**
             * 默认不开启消费端失败重试
             */
            private Integer retries = 0;
            /**
             * 集群模式默认为failover
             */
            private String cluster = "failover";
            /**
             * 引用服务的接口全限定类名
             */
            private String interfaceName;

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

            public Integer getRetries() {
                return retries;
            }

            public void setRetries(Integer retries) {
                this.retries = retries;
            }

            public String getCluster() {
                return cluster;
            }

            public void setCluster(String cluster) {
                this.cluster = cluster;
            }

            public String getInterfaceName() {
                return interfaceName;
            }

            public void setInterfaceName(String interfaceName) {
                this.interfaceName = interfaceName;
            }

            @Override
            public String toString() {
                return "Reference{" +
                        "timeout=" + timeout +
                        ", invokeType='" + invokeType + '\'' +
                        ", retries=" + retries +
                        ", cluster='" + cluster + '\'' +
                        ", interfaceName='" + interfaceName + '\'' +
                        '}';
            }
        }
    }
}
