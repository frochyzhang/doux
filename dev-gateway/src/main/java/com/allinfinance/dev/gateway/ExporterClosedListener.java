package com.allinfinance.dev.gateway;

import com.alipay.sofa.rpc.boot.container.ConsumerConfigContainer;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.rpc.bootstrap.Bootstraps;
import com.alipay.sofa.rpc.bootstrap.ConsumerBootstrap;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.alipay.sofa.runtime.spi.binding.Binding;
import com.allinfinance.dev.gateway.event.ExporterClosedEvent;
import com.allinfinance.dev.gateway.factory.AppProcessFactory;
import com.allinfinance.dev.gateway.netty.HttpServer;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/28 10:59
 */
@Component
public class ExporterClosedListener implements ApplicationListener<ExporterClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ExporterClosedListener.class);

    @Autowired
    private AppProcessFactory appProcessFactory;
    @Autowired
    private ConsumerConfigContainer consumerConfigContainer;

    @Override
    public void onApplicationEvent(ExporterClosedEvent event) {
        logger.info("ExporterClosedEvent fired...");

        String uniqueId = event.getAppUniqueId();
        logger.warn("{}已掉线，网关订阅移除", uniqueId);

        ReferenceParam<ProcessService> referenceParam = new ReferenceParam<>();
        BoltBindingParam boltBindingParam = new BoltBindingParam();
        boltBindingParam.setLoadBalancer("roundRobin");
        referenceParam.setBindingParam(boltBindingParam);

        referenceParam.setInterfaceType(ProcessService.class);
        referenceParam.setUniqueId(uniqueId);

        appProcessFactory.removeReference(referenceParam);

        ConcurrentMap<Binding, ConsumerConfig> consumerConfigMap = consumerConfigContainer.getConsumerConfigMap();
        int subscribeSize = consumerConfigMap.values().stream()
                .filter(cc -> cc.getUniqueId().equals(uniqueId))
                .mapToInt(cc -> {
                    ConsumerBootstrap bootstrap = Bootstraps.from(cc);
                    return bootstrap.subscribe().size();
                }).sum();
        if (subscribeSize == 0) {
            logger.warn("所有{}的应用均已下线，现移除端口监听!", uniqueId);
            HttpServer.getInstance(uniqueId).shutdown();
        }
    }
}
