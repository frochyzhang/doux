package com.allinfinance.dev.gateway.service;

import com.allinfinance.dev.gateway.event.ExporterClosedEvent;
import com.allinfinance.dev.gateway.factory.GateClientFactoryAware;
import com.allinfinance.dev.rpc.scaffold.api.AppRegistrarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/28 14:32
 */
@Service
public class AppRegistrarServiceImpl implements AppRegistrarService {
    private static final Logger logger = LoggerFactory.getLogger(AppRegistrarServiceImpl.class);

    @Autowired
    private GateClientFactoryAware gateClientFactoryAware;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Boolean register(String appUniqueId) {
        if (gateClientFactoryAware.registerConsumer(appUniqueId)) {
            logger.info("[ {} ]应用注册成功!", appUniqueId);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public void signExporterClosedEvent(String appUniqueId) {
        applicationEventPublisher.publishEvent(new ExporterClosedEvent(new Object(), appUniqueId));
    }
}
