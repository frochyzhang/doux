package com.allinfinance.dev.rpc.scaffold.config;

import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.core.exception.SofaRouteException;
import com.allinfinance.dev.rpc.scaffold.api.AppRegistrarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/24 23:09
 */
@ConditionalOnBean(value = {RpcGatewayBootstrapRegistrar.class})
@Component
public class ExporterStatedListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ExporterStatedListener.class);

    @Autowired
    private RpcConfigurationProperties rpcConfigurationProperties;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        logger.info("applicationStartedEvent fired!");

        RegistryConfig registryConfig = SofaAPIConfig.getRegistryConfig(rpcConfigurationProperties.getBootstrap().getGateRegistry());

        // 2 调用网关的注册服务
        logger.info("开始调用网关注册服务");
        AppRegistrarService appRegistrarService = SofaAPIConfig.referProxyConsumerRef(registryConfig, AppRegistrarService.class, 3000, "foreach", 3);
        Thread gateRegistryThread = new Thread(() -> {
            Boolean registerResult = null;
            while (null == registerResult) {
                try {
                    registerResult = appRegistrarService.register(rpcConfigurationProperties.getBootstrap().getAppUniqueId());
                } catch (SofaRouteException sofaRouteException) {
                    logger.warn("网关不存在，10s后重试+1");
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    logger.error("调用网关注册服务失败!", e);
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
    }
}
