package com.allinfinance.dev.rpc.scaffold.config;

import cn.hutool.core.net.NetUtil;
import com.alibaba.nacos.api.exception.NacosException;
import com.alipay.sofa.rpc.config.ApplicationConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.alipay.sofa.rpc.core.exception.SofaRouteException;
import com.allinfinance.dev.rpc.scaffold.api.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/27 16:36
 */
@ConditionalOnProperty(value = RpcConfigurationProperties.Bootstrap.BOOT_ENABLE, havingValue = "true")
@Configuration
public class RpcGatewayBootstrapRegistrar implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcGatewayBootstrapRegistrar.class);
    @Autowired
    private RpcConfigurationProperties rpcConfigurationProperties;

    @Autowired(required = false)
    private ProcessService processService;

    @Override
    public void afterPropertiesSet() throws NacosException {
        if (processService == null) {
            throw new NullPointerException("未匹配到ProcessService实现!");
        }
        RpcConfigurationProperties.Bootstrap bootstrap = rpcConfigurationProperties.getBootstrap();
        if (StringUtils.hasText(bootstrap.getAppUniqueId())
                && StringUtils.hasText(bootstrap.getGateRegistry())) {

            RegistryConfig registryConfig = SofaAPIConfig.getRegistryConfig(rpcConfigurationProperties.getBootstrap().getGateRegistry());

            // 1 processService注册到注册中心，需以uniqueId区分不同系统
            ApplicationConfig applicationConfig = new ApplicationConfig();
            applicationConfig.setAppName(rpcConfigurationProperties.getBootstrap().getAppUniqueId());
            ServerConfig serverConfig = SofaAPIConfig.getServerConfig(NetUtil.getUsableLocalPort(12001, 12999));

            SofaAPIConfig.initProviderConfig(serverConfig, registryConfig, applicationConfig, rpcConfigurationProperties.getBootstrap().getAppUniqueId(), processService);

            ProcessService testProcessService = SofaAPIConfig.referProxyConsumerRef(rpcConfigurationProperties.getBootstrap().getAppUniqueId(), registryConfig, ProcessService.class, 3000, "foreach", 3);

            Boolean verifyResult = null;
            while (true) {
                try {
                    verifyResult = testProcessService.verify();
                } catch (SofaRouteException sofaRouteException) {
                    logger.warn("ProcessService未发布成功，等待10s后重试");
                }
                if (verifyResult != null) {
                    logger.info("{}业务处理服务注册成功", rpcConfigurationProperties.getBootstrap().getAppUniqueId());
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new RuntimeException("TCP网关注册开关已打开，未设置必要参数!");
        }
    }
}
