package com.allinfinance.dev.rpc.scaffold.provider;

import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ServiceClient;
import com.alipay.sofa.runtime.api.client.param.BindingParam;
import com.alipay.sofa.runtime.api.client.param.ServiceParam;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qipeng
 * @date 2021/12/30 9:22
 */
@Component
public class ProviderScanSupport implements BeanPostProcessor, ClientFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(ProviderScanSupport.class);

    @Autowired
    private RpcConfigurationProperties rpcConfigurationProperties;

    private ClientFactory clientFactory;


    /**
     * Set the instance of {@link ClientFactory} to the Spring bean that implement this interface.
     *
     * @param clientFactory ClientFactory The instance of {@link ClientFactory}
     */
    @Override
    public void setClientFactory(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (rpcConfigurationProperties.getProviderPackage() == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("provider 配置未找到！");
            }
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }

        String beanType = bean.getClass().getName();
        String providerPackage = rpcConfigurationProperties.getProviderPackage();
        if (beanType.contains(providerPackage)) {
            if (logger.isDebugEnabled()) {
                logger.debug("发布Bean: beanName=[{}], beanType=[{}]", beanName, beanType);
            }
            ServiceClient serviceClient = clientFactory.getClient(ServiceClient.class);

            ServiceParam serviceParam = new ServiceParam();
            serviceParam.setInstance(bean);
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            serviceParam.setInterfaceType(interfaces[0]);

            List<BindingParam> params = new ArrayList<>();
            BindingParam serviceBindingParam = new BoltBindingParam();
            params.add(serviceBindingParam);
            serviceParam.setBindingParams(params);
            serviceClient.service(serviceParam);

        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
