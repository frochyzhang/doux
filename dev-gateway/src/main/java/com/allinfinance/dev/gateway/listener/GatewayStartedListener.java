package com.allinfinance.dev.gateway.listener;

import com.allinfinance.dev.gateway.factory.AppProcessFactory;
import com.allinfinance.dev.gateway.factory.GateClientFactoryAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author huanghf
 * @date 2022/4/26 19:33
 */
@Component
@ConditionalOnBean(value = {GateClientFactoryAware.class})
public class GatewayStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GatewayStartedListener.class);

    @Value("${com.alipay.sofa.rpc.registry-address}")
    private String registryAddress;

    @Autowired
    private GateClientFactoryAware gateClientFactoryAware;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        logger.info("网关启动完成，开始订阅应用业务处理服务");
        String server = registryAddress.substring(registryAddress.indexOf("://") + 3);
        AppProcessFactory.getServiceList(server)
                .forEach(service -> {
                    String uniqueId = service.split(":")[1];
                    if (gateClientFactoryAware.registerConsumer(uniqueId)) {
                        logger.info("[ {} ]应用注册成功!", uniqueId);
                    } else {
                        logger.error("[ {} ]应用注册失败!", uniqueId);
                    }
                });
        logger.info("应用业务处理服务订阅完成");
    }
}
