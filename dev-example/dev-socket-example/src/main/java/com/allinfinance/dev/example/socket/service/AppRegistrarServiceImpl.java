package com.allinfinance.dev.example.socket.service;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.allinfinance.dev.example.socket.factory.AppProcessFactory;
import com.allinfinance.dev.rpc.scaffold.api.AppRegistrarService;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
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
    private AppProcessFactory appProcessFactory;

    @Override
    public Boolean register(RpcConfigurationProperties.Bootstrap bootstrap) {

        if (appProcessFactory.checkIfExist(bootstrap.getAppUniqueId())) {
            logger.info("[ {} ]无需重复注册", bootstrap.getAppUniqueId());
            return Boolean.TRUE;
        }
        RegistryConfig registryConfig = new RegistryConfig()
                .setProtocol(RpcConfigurationProperties.Bootstrap.REGISTRY_PROTOCOL)
                .setAddress(bootstrap.getGateRegistry());

        // todo: 还得监听provider服务健康情况
        ConsumerConfig<ProcessService> consumerConfig = new ConsumerConfig<ProcessService>()
                .setInterfaceId(ProcessService.class.getName())
                .setUniqueId(bootstrap.getAppUniqueId())
                .setTimeout(30000)
                .setConnectTimeout(30000)
                .setRegistry(registryConfig);
        ProcessService processService = consumerConfig.refer();

        if (processService.verify()) {
            logger.info("[ {} ]业务处理服务订阅成功!", bootstrap.getAppUniqueId());
        }
        // 监听端口

        // 监听完成

        appProcessFactory.register(bootstrap.getAppUniqueId(), processService);


        return Boolean.TRUE;
    }
}
