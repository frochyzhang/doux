package com.allinfinance.dev.example.socket.service;

import com.allinfinance.dev.example.socket.factory.GateClientFactoryAware;
import com.allinfinance.dev.rpc.scaffold.api.AppRegistrarService;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Boolean register(RpcConfigurationProperties.Bootstrap bootstrap) {
        String appUniqueId = bootstrap.getAppUniqueId();
        if (gateClientFactoryAware.registerConsumer(appUniqueId)) {
            logger.info("[ {} ]应用注册成功!", appUniqueId);
        }
        return Boolean.TRUE;
    }
}
