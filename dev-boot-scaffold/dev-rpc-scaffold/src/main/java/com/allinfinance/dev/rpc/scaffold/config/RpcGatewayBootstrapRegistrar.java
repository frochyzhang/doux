package com.allinfinance.dev.rpc.scaffold.config;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.alipay.sofa.rpc.core.exception.SofaRouteException;
import com.allinfinance.dev.rpc.scaffold.api.AppRegistrarService;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/27 16:36
 */
@ConditionalOnProperty(value = RpcConfigurationProperties.Bootstrap.BOOT_ENABLE, havingValue = "true")
@Configuration
public class RpcGatewayBootstrapRegistrar implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcGatewayBootstrapRegistrar.class);
    @Autowired
    private RpcConfigurationProperties rpcConfigurationProperties;

    @Autowired(required = false)
    private ProcessService processService;

    @Override
    public void afterPropertiesSet() {
        if (processService == null) {
            throw new NullPointerException("未匹配到ProcessService实现!");
        }
        RpcConfigurationProperties.Bootstrap bootstrap = rpcConfigurationProperties.getBootstrap();
        if (StringUtils.hasText(bootstrap.getAppUniqueId())
                && StringUtils.hasText(bootstrap.getGateRegistry())) {

            RegistryConfig registryConfig = new RegistryConfig()
                    .setProtocol(RpcConfigurationProperties.Bootstrap.REGISTRY_PROTOCOL)
                    .setAddress(rpcConfigurationProperties.getBootstrap().getGateRegistry());

            // 1 processService注册到注册中心，需以uniqueId区分不同系统
            ServerConfig serverConfig = new ServerConfig()
                    .setPort(12200)
                    .setProtocol(RpcConfigurationProperties.Bootstrap.TRANSPORT_PROTOCOL);
            ProviderConfig<ProcessService> providerConfig = new ProviderConfig<ProcessService>()
                    .setInterfaceId(ProcessService.class.getName())
                    .setRef(processService)
                    .setUniqueId(rpcConfigurationProperties.getBootstrap().getAppUniqueId())
                    .setServer(serverConfig)
                    .setRegistry(registryConfig);
            providerConfig.export();
            logger.info("{}业务处理服务注册成功", rpcConfigurationProperties.getBootstrap().getAppUniqueId());

            // 2 调用网关的注册服务
            ConsumerConfig<AppRegistrarService> consumerConfig = new ConsumerConfig<AppRegistrarService>()
                    .setInterfaceId(AppRegistrarService.class.getName())
                    .setTimeout(3000)
                    .setConnectTimeout(3000)
                    .setRetries(3)
                    .setRegistry(registryConfig);
            AppRegistrarService appRegistrarService = consumerConfig.refer();
            Thread gateRegistryThread = new Thread(() -> {
                Boolean registerResult = null;
                while (null == registerResult) {
                    try {
                        registerResult = appRegistrarService.register(rpcConfigurationProperties.getBootstrap());
                    } catch (SofaRouteException sofaRouteException) {
                        logger.warn("网关不存在，10s后重试+1");
                    }
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (registerResult) {
                    logger.info("应用{}注册到网关成功!", rpcConfigurationProperties.getBootstrap().getAppUniqueId());
                } else {
                    logger.info("应用{}注册到网关失败!", rpcConfigurationProperties.getBootstrap().getAppUniqueId());
                    throw new RuntimeException("应用注册失败!");
                }
            }, "gate-registry-thread");

            gateRegistryThread.setDaemon(true);
            gateRegistryThread.start();

        } else {
            throw new RuntimeException("TCP网关注册开关已打开，未设置必要参数!");
        }
    }

}
