package com.allinfinance.dev.gateway;

import com.allinfinance.dev.gateway.factory.AppProcessFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/28 10:59
 */
@Component
public class GatewayAppStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GatewayAppStartedListener.class);

    @Value("${com.alipay.sofa.rpc.registry-address}")
    private String registryAddress;

    @Autowired
    private AppProcessFactory appProcessFactory;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("GatewayAppListener stated...");

        Thread appRemoveThread = new Thread(() -> {
            while (true) {
                String server = registryAddress.substring(registryAddress.indexOf("://") + 3);

                List<String> appList = AppProcessFactory.getServiceList(server).stream()
                        .map(service -> service.split(":")[1]).collect(Collectors.toList());

                appProcessFactory.checkAndProcess(appList);

                try {
                    TimeUnit.SECONDS.sleep(60);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "app-remove-thread");

        appRemoveThread.setDaemon(true);
        appRemoveThread.start();
    }
}
