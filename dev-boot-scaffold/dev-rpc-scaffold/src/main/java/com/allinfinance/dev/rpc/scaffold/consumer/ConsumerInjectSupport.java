package com.allinfinance.dev.rpc.scaffold.consumer;

import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.BindingParam;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.allinfinance.dev.core.util.common.BeanUtils;
import com.allinfinance.dev.core.util.http.client.IHttpClientService;
import com.allinfinance.dev.core.util.socket.client.ISocketClientService;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import com.allinfinance.dev.rpc.scaffold.config.SofaAPIConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2021/12/30 9:58
 */
@Component
public class ConsumerInjectSupport implements SmartInstantiationAwareBeanPostProcessor, ClientFactoryAware, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerInjectSupport.class);
    @Autowired
    private RpcConfigurationProperties rpcConfigurationProperties;

    @Autowired
    private CustomBeanFactoryPostProcessor customBeanFactoryPostProcessor;

    /**
     * Set the instance of {@link ClientFactory} to the Spring bean that implement this interface.
     *
     * @param clientFactory ClientFactory The instance of {@link ClientFactory}
     */
    @Override
    public void setClientFactory(ClientFactory clientFactory) {
        if (rpcConfigurationProperties.getReferenceList() == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("consumer 配置未找到！");
            }
            return;
        }
        ReferenceClient referenceClient = clientFactory.getClient(ReferenceClient.class);
        ReferenceParam referenceParam = new ReferenceParam<>();
        BindingParam refBindingParam = new BoltBindingParam();
        referenceParam.setBindingParam(refBindingParam);

        for (String className : rpcConfigurationProperties.getReferenceList()) {
            if (className.equals(IHttpClientService.class.getName())
                    || className.equals(ISocketClientService.class.getName())) {
                continue;
            }
            try {
                referenceParam.setInterfaceType(Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Object proxy = referenceClient.reference(referenceParam);
            String beanName = BeanUtils.getBeanNameWithImpl(className);
            if (logger.isDebugEnabled()) {
                logger.debug("注入Bean: beanName=[{}], beanType=[{}]", beanName, className);
            }
            customBeanFactoryPostProcessor.getConfigurableListableBeanFactory().registerSingleton(beanName, proxy);
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (StringUtils.isBlank(rpcConfigurationProperties.getBootstrap().getGateRegistry())) {
            return;
        }
        RegistryConfig registryConfig = SofaAPIConfig.getRegistryConfig(rpcConfigurationProperties.getBootstrap().getGateRegistry());

        IHttpClientService iHttpClientService = SofaAPIConfig.referProxyConsumerRef(registryConfig, IHttpClientService.class, 3000);
        customBeanFactoryPostProcessor.getConfigurableListableBeanFactory().registerSingleton(BeanUtils.getBeanNameWithImpl(IHttpClientService.class.getName()), iHttpClientService);
        logger.info("注入HttpClientService完成！");

        ISocketClientService iSocketClientService = SofaAPIConfig.referProxyConsumerRef(registryConfig, ISocketClientService.class, 3000);
        customBeanFactoryPostProcessor.getConfigurableListableBeanFactory().registerSingleton(BeanUtils.getBeanNameWithImpl(ISocketClientService.class.getName()), iSocketClientService);
        logger.info("注入ISocketClientService完成！");
    }
}
