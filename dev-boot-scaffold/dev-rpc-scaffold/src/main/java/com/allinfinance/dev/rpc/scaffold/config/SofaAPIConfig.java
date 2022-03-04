package com.allinfinance.dev.rpc.scaffold.config;

import com.alipay.sofa.rpc.config.*;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/24 17:40
 */
public class SofaAPIConfig {
    public static void initProviderConfig(ServerConfig serverConfig, RegistryConfig registryConfig, ApplicationConfig applicationConfig, String appUniqueId, ProcessService refService) {
        ProviderConfig<ProcessService> providerConfig = new ProviderConfig<ProcessService>()
                .setInterfaceId(ProcessService.class.getName())
                .setRef(refService)
                .setUniqueId(appUniqueId)
                .setServer(serverConfig)
                .setApplication(applicationConfig)
                .setRegistry(registryConfig);
        providerConfig.export();
    }

    public static <T> T referProxyConsumerRef(RegistryConfig registryConfig, Class<T> tClass, int timeout, String cluster, int retries) {
        ConsumerConfig<T> consumerConfig = new ConsumerConfig<T>()
                .setInterfaceId(tClass.getName())
                .setTimeout(timeout)
                .setConnectTimeout(timeout)
                .setCluster(cluster)
                .setRetries(retries)
                .setRegistry(registryConfig);
        return consumerConfig.refer();
    }

    public static <T> T referProxyConsumerRef(String uniqueId, RegistryConfig registryConfig, Class<T> tClass, int timeout, String cluster, int retries) {
        ConsumerConfig<T> consumerConfig = new ConsumerConfig<T>()
                .setInterfaceId(tClass.getName())
                .setTimeout(timeout)
                .setConnectTimeout(timeout)
                .setCluster(cluster)
                .setRetries(retries)
                .setUniqueId(uniqueId)
                .setRegistry(registryConfig);
        return consumerConfig.refer();
    }

    public static <T> T referProxyConsumerRef(RegistryConfig registryConfig, Class<T> tClass, int timeout) {
        return referProxyConsumerRef(registryConfig, tClass, timeout, "failover", 3);
    }


    public static ServerConfig getServerConfig(Integer port) {
        return new ServerConfig()
                .setPort(port)
                .setProtocol(RpcConfigurationProperties.Bootstrap.TRANSPORT_PROTOCOL);
    }

    public static RegistryConfig getRegistryConfig(String registryAddress) {
        return new RegistryConfig()
                .setProtocol(RpcConfigurationProperties.Bootstrap.REGISTRY_PROTOCOL)
                .setAddress(registryAddress);
    }
}
