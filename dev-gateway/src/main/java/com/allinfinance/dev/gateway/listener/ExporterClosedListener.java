package com.allinfinance.dev.gateway.listener;

import com.alipay.sofa.rpc.boot.container.ConsumerConfigContainer;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.rpc.bootstrap.Bootstraps;
import com.alipay.sofa.rpc.bootstrap.ConsumerBootstrap;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.alipay.sofa.runtime.spi.binding.Binding;
import com.allinfinance.dev.core.bean.MinaSocketBean;
import com.allinfinance.dev.gateway.event.ExporterClosedEvent;
import com.allinfinance.dev.gateway.factory.AppProcessFactory;
import com.allinfinance.dev.gateway.netty.HttpServer;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import com.allinfinance.dev.socket.config.ShortSwitchServer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

        String uniqueId = event.getBootstrap().getAppUniqueId();
        //logger.warn("{}已掉线，网关订阅移除", uniqueId);

        //ReferenceParam<ProcessService> referenceParam = new ReferenceParam<>();
        //BoltBindingParam boltBindingParam = new BoltBindingParam();
        //boltBindingParam.setLoadBalancer("roundRobin");
        //referenceParam.setBindingParam(boltBindingParam);
        //
        //referenceParam.setInterfaceType(ProcessService.class);
        //referenceParam.setUniqueId(uniqueId);

        // FIXME: 2022/5/6 其中一个节点重启不能移除订阅
        //appProcessFactory.removeReference(referenceParam);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.error("移除订阅异常", e);
        }

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
            //移除http端口监听
            Optional.ofNullable(HttpServer.getInstance(uniqueId))
                    .ifPresent(httpServerList -> httpServerList
                            .stream()
                            .filter(httpServer -> {
                                //是否有其它监听这个端口的应用
                                List<RpcConfigurationProperties.Bootstrap> bootstrapList = appProcessFactory.getCompares()
                                        .values()
                                        .stream()
                                        .filter(bootstrap -> !uniqueId.equals(bootstrap.getAppUniqueId()) && bootstrap.getAppList()
                                                .stream()
                                                .anyMatch(appConfigList -> appConfigList.getListenPort().equals(httpServer.getPort())))
                                        .collect(Collectors.toList());
                                return CollectionUtils.isEmpty(bootstrapList);
                                // TODO: 2022/5/6 注册时已经监听此端口该如何处理，APP_SERVER_MAP已经存在该uniqueId的list，那么关闭时不会进行shutdown，后续该如何处理
                            }).forEach(httpServer -> httpServer.shutdown(uniqueId)));
            //移除tcp端口监听
            event.getBootstrap().getAppList()
                    .stream()
                    .filter(appConfigList -> RpcConfigurationProperties.Bootstrap.AppConfigList.Type.TCP.equals(appConfigList.getType()))
                    .forEach(appConfigList -> {
                        MinaSocketBean minaSocketBean = new MinaSocketBean();
                        minaSocketBean.setPort(appConfigList.getListenPort());
                        new ShortSwitchServer().closeMinaServer(minaSocketBean);
                    });
        } else {
            logger.info("[ {} ]应用未全部下线", uniqueId);
        }
        logger.info("ExporterClosedEvent Finished...");
    }
}
