package com.allinfinance.dev.rpc.scaffold.config;

import cn.hutool.extra.spring.SpringUtil;
import com.alipay.sofa.jraft.error.RemotingException;
import com.allinfinance.dev.rpc.scaffold.dto.ExporterRegistrarRequest;
import com.allinfinance.dev.rpc.scaffold.processor.BusinessProcessedFactory;
import com.allinfinance.dev.rpc.scaffold.processor.BusinessProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/24 23:09
 */
@ConditionalOnBean(value = {RpcGatewayBootstrapRegistrar.class})
@Component
public class ExporterStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ExporterStartedListener.class);

    @Autowired
    private RpcConfigurationProperties rpcConfigurationProperties;

    @Autowired
    private RaftRpcClientConfig raftRpcClientConfig;

    @Autowired
    private BusinessProcessedFactory businessProcessedFactory;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        logger.info("exporter前置启动完成，开始加载BusinessProcessor");
        Arrays.stream(SpringUtil.getBeanNamesForType(BusinessProcessor.class))
                .forEach(processorBeanName -> businessProcessedFactory.register(SpringUtil.getBean(processorBeanName)));

        // 调用网关的注册服务
        logger.info("BusinessProcessor加载完成，开始调用网关注册服务");
        Thread gateRegistryThread = new Thread(() -> {
            Boolean registerResult = null;
            while (true) {
                try {
                    registerResult = raftRpcClientConfig.invokeSync(new ExporterRegistrarRequest(rpcConfigurationProperties.getBootstrap()), 5000);
                } catch (InterruptedException e) {
                    logger.error("调用网关注册服务异常", e);
                    Thread.currentThread().interrupt();
                } catch (TimeoutException | RemotingException e) {
                    logger.error("调用网关注册服务异常", e);
                }
                if (registerResult == null) {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        logger.error("调用网关注册服务失败!", e);
                        Thread.currentThread().interrupt();
                    }
                } else if (registerResult) {
                    logger.info("应用[{}]注册到网关成功!", rpcConfigurationProperties.getBootstrap().getAppUniqueId());
                    break;
                } else {
                    logger.error("应用[{}]注册到网关失败!", rpcConfigurationProperties.getBootstrap().getAppUniqueId());
                    throw new RuntimeException("应用注册失败!");
                }
            }
        }, "gate-registry-thread");

        gateRegistryThread.setDaemon(true);
        gateRegistryThread.start();
    }
}
