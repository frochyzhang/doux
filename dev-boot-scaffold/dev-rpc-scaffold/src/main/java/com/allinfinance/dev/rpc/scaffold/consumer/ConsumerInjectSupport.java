package com.allinfinance.dev.rpc.scaffold.consumer;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.allinfinance.dev.common.util.common.BeanUtils;
import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import com.allinfinance.dev.rpc.scaffold.config.SofaAPIConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
        if (rpcConfigurationProperties.getConsumer() == null
                || CollectionUtil.isEmpty(rpcConfigurationProperties.getConsumer().getReferenceList())) {
            if (logger.isDebugEnabled()) {
                logger.debug("consumer 配置未找到！");
            }
            return;
        }

        ReferenceClient referenceClient = clientFactory.getClient(ReferenceClient.class);
        ReferenceParam referenceParam = new ReferenceParam<>();
        BoltBindingParam refBindingParam = new BoltBindingParam();
        referenceParam.setBindingParam(refBindingParam);

        for (RpcConfigurationProperties.Consumer.Reference reference : rpcConfigurationProperties.getConsumer().getReferenceList()) {
            try {
                referenceParam.setInterfaceType(Class.forName(reference.getInterfaceName()));
            } catch (ClassNotFoundException e) {
                logger.error("未找到接口名对应类【{}】，请检查配置项", reference.getInterfaceName());
                continue;
            }

            Optional.ofNullable(reference.getTimeout()).ifPresent(refBindingParam::setTimeout);
            Optional.ofNullable(reference.getInvokeType()).ifPresent(refBindingParam::setType);
            Optional.ofNullable(reference.getRetries()).ifPresent(refBindingParam::setRetries);

            Object proxy = referenceClient.reference(referenceParam);
            String beanName = BeanUtils.getBeanNameWithImpl(reference.getInterfaceName());
            if (logger.isDebugEnabled()) {
                logger.debug("注入Bean: beanName=[{}], beanType=[{}]", beanName, reference.getInterfaceName());
            }
            customBeanFactoryPostProcessor.getConfigurableListableBeanFactory().registerSingleton(beanName, proxy);
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (rpcConfigurationProperties.getConsumer() != null) {
            RpcConfigurationProperties.Consumer consumer = rpcConfigurationProperties.getConsumer();
            if (StringUtils.isBlank(consumer.getCommonReferenceRegistry())) {
                logger.error("未配置公共服务注册中心地址，请检查配置项");
                return;
            }
            List<RpcConfigurationProperties.Consumer.Reference> commonReferenceList = consumer.getCommonReferenceList();
            if (CollectionUtils.isNotEmpty(commonReferenceList)) {
                logger.info("开始注入公共服务，服务列表: {}", commonReferenceList);
                RegistryConfig registryConfig = SofaAPIConfig.getRegistryConfig(consumer.getCommonReferenceRegistry());
                commonReferenceList
                        .forEach(commonReference -> {
                            try {
                                Object consumerRef = SofaAPIConfig.referProxyConsumerRef(registryConfig, Class.forName(commonReference.getInterfaceName()),
                                        commonReference.getTimeout(), commonReference.getCluster(), commonReference.getRetries());
                                customBeanFactoryPostProcessor.getConfigurableListableBeanFactory().registerSingleton(BeanUtils.getBeanNameWithImpl(commonReference.getInterfaceName()), consumerRef);
                                logger.info("公共服务【{}】注入完成", commonReference.getInterfaceName());
                            } catch (ClassNotFoundException e) {
                                logger.error("未寻找到公共服务类【{}】，请检查配置项", commonReference.getInterfaceName());
                            }
                        });
            }
        }
    }
}
