package cn.lezoo.doux.rpc.scaffold.provider;

import cn.lezoo.doux.rpc.scaffold.config.RpcConfigurationProperties;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ServiceClient;
import com.alipay.sofa.runtime.api.client.param.BindingParam;
import com.alipay.sofa.runtime.api.client.param.ServiceParam;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

        if (ObjectUtils.isEmpty(rpcConfigurationProperties.getProvider())) {
            if (logger.isDebugEnabled()) {
                logger.debug("provider 配置未找到！");
            }
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }
        RpcConfigurationProperties.Provider provider = rpcConfigurationProperties.getProvider();
        if (StringUtils.isBlank(provider.getServicePackage())) {
            if (logger.isDebugEnabled()) {
                logger.debug("提供方的包路径 配置未找到！");
            }
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }

        String beanType = bean.getClass().getName();
        String providerPackage = provider.getServicePackage();
        if (beanType.contains(providerPackage)) {
            if (CollectionUtils.isNotEmpty(provider.getExcludeServiceList())) {
                if (provider.getExcludeServiceList().contains(beanType)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("该Bean不发布: beanName=[{}], beanType=[{}]", beanName, beanType);
                    }
                    return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("发布Bean: beanName=[{}], beanType=[{}]", beanName, beanType);
            }
            ServiceClient serviceClient = clientFactory.getClient(ServiceClient.class);

            ServiceParam serviceParam = new ServiceParam();
            serviceParam.setInstance(bean);
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            if (interfaces.length != 0) {
                serviceParam.setInterfaceType(interfaces[0]);

                List<BindingParam> params = new ArrayList<>();
                BindingParam serviceBindingParam = new BoltBindingParam();
                params.add(serviceBindingParam);
                serviceParam.setBindingParams(params);
                serviceClient.service(serviceParam);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
