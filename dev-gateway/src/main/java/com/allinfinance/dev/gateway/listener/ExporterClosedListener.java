package com.allinfinance.dev.gateway.listener;

import com.alipay.sofa.rpc.boot.container.ConsumerConfigContainer;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.rpc.bootstrap.Bootstraps;
import com.alipay.sofa.rpc.bootstrap.ConsumerBootstrap;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.alipay.sofa.runtime.spi.binding.Binding;
import com.allinfinance.dev.gateway.event.ExporterClosedEvent;
import com.allinfinance.dev.gateway.factory.AppProcessFactory;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

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

        // FIXME: 2022/5/7 还是会出现实际已全部下线但是并未进行移除订阅问题
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.error("ExporterClosedEvent延时处理异常", e);
        }

        String uniqueId = event.getBootstrap().getAppUniqueId();
        ConcurrentMap<Binding, ConsumerConfig> consumerConfigMap = consumerConfigContainer.getConsumerConfigMap();
        int subscribeSize = consumerConfigMap.values().stream()
                .filter(cc -> cc.getUniqueId().equals(uniqueId))
                .mapToInt(cc -> {
                    ConsumerBootstrap bootstrap = Bootstraps.from(cc);
                    return bootstrap.subscribe().size();
                }).sum();
        if (subscribeSize == 0) {
            logger.warn("所有{}的应用均已下线，移除网关订阅，取消端口监听!", uniqueId);
            ReferenceParam<ProcessService> referenceParam = new ReferenceParam<>();
            BoltBindingParam boltBindingParam = new BoltBindingParam();
            boltBindingParam.setLoadBalancer("roundRobin");
            referenceParam.setBindingParam(boltBindingParam);
            referenceParam.setInterfaceType(ProcessService.class);
            referenceParam.setUniqueId(uniqueId);
            //移除网关订阅
            appProcessFactory.removeReference(referenceParam);
            //移除端口监听
            appProcessFactory.removePortMonitor(uniqueId);
            //移除配置信息，包括compares和appUrlMap
            appProcessFactory.removeBootstrap(uniqueId);
        } else {
            logger.info("[ {} ]应用未全部下线", uniqueId);
        }
        logger.info("ExporterClosedEvent Finished...");
    }
}
