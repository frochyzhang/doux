package com.allinfinance.dev.gateway;

import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.allinfinance.dev.gateway.event.ExporterClosedEvent;
import com.allinfinance.dev.gateway.factory.AppProcessFactory;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/28 10:59
 */
@Component
public class ExporterClosedListener implements ApplicationListener<ExporterClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ExporterClosedListener.class);

    @Autowired
    private AppProcessFactory appProcessFactory;

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
    }
}
