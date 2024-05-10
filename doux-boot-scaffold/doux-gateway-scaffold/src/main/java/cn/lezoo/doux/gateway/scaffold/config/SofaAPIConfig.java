package cn.lezoo.doux.gateway.scaffold.config;

import cn.lezoo.doux.gateway.scaffold.api.ProcessService;
import com.alipay.sofa.rpc.config.ApplicationConfig;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;

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
                .setRegistry(registryConfig)
                .setInJVM(false);
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
                .setRegistry(registryConfig)
                .setInJVM(false);
        return consumerConfig.refer();
    }

    public static <T> T referProxyConsumerRef(String uniqueId, RegistryConfig registryConfig, Class<T> tClass, String cluster) {
        ConsumerConfig<T> consumerConfig = new ConsumerConfig<T>()
                .setUniqueId(uniqueId)
                .setRegistry(registryConfig)
                .setInterfaceId(tClass.getName())
                .setCluster(cluster)
                .setInJVM(false);
        return consumerConfig.refer();
    }

    public static <T> T referProxyConsumerRef(RegistryConfig registryConfig, Class<T> tClass, int timeout) {
        return referProxyConsumerRef(registryConfig, tClass, timeout, "failover", 0);
    }

    public static ServerConfig getServerConfig(Integer port) {
        return new ServerConfig()
                .setPort(port)
                .setProtocol(Bootstrap.TRANSPORT_PROTOCOL);
    }

    public static RegistryConfig getRegistryConfig(String registryAddress) {
        return new RegistryConfig()
                .setProtocol(Bootstrap.REGISTRY_PROTOCOL)
                .setAddress(registryAddress);
    }
}
