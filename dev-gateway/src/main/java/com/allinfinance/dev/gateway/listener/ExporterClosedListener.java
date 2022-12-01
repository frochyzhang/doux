package com.allinfinance.dev.gateway.listener;

import com.alipay.sofa.rpc.boot.container.ConsumerConfigContainer;
import com.alipay.sofa.rpc.bootstrap.Bootstraps;
import com.alipay.sofa.rpc.bootstrap.ConsumerBootstrap;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.runtime.spi.binding.Binding;
import com.allinfinance.dev.gateway.factory.AppProcessFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/28 10:59
 */
@Component
public class ExporterClosedListener implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ExporterClosedListener.class);

    @Autowired
    private AppProcessFactory appProcessFactory;
    @Autowired
    private ConsumerConfigContainer consumerConfigContainer;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            while (true) {
                appProcessFactory.getPROCESS_SYNC_CACHE().keySet().forEach(uniqueId -> {
                    ConcurrentMap<Binding, ConsumerConfig> consumerConfigMap = consumerConfigContainer.getConsumerConfigMap();
                    int subscribeSize = consumerConfigMap.values().stream()
                            .filter(cc -> cc.getUniqueId().equals(uniqueId))
                            .mapToInt(cc -> {
                                ConsumerBootstrap bootstrap = Bootstraps.from(cc);
                                return bootstrap.subscribe().size();
                            }).sum();
                    if (subscribeSize == 0) {
                        logger.warn("所有{}的应用均已下线，移除网关订阅和配置信息，取消端口监听!", uniqueId);
                        appProcessFactory.removeAll(uniqueId);
                    } else {
                        logger.info("[ {} ]应用未全部下线", uniqueId);
                    }
                });
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException ignore) {
                }
            }
        }, "exporter-monitor-thread").start();
    }
}
