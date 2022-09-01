package com.allinfinance.dev.gateway.service;

import com.allinfinance.dev.gateway.factory.GateClientFactoryAware;
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
        try {
            if (gateClientFactoryAware.registerConsumer(bootstrap)) {
                logger.info("[ {} ]应用注册成功!", bootstrap.getAppUniqueId());
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            logger.error("[ {} ]应用注册异常!", bootstrap.getAppUniqueId(), e);
            return Boolean.FALSE;
        }
        logger.error("[ {} ]应用注册失败!", bootstrap.getAppUniqueId());
        return Boolean.FALSE;
    }
}
