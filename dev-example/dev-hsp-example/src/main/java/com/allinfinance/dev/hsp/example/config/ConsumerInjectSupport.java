package com.allinfinance.dev.hsp.example.config;


import com.allinfinance.dev.common.hsp.api.SignatureService;
import com.allinfinance.dev.common.util.common.BeanUtils;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author huanghf
 * @date 2022/7/4 14:40
 */
//@Component("dubboConsumerInjectSupport")
public class ConsumerInjectSupport implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerInjectSupport.class);

    private ApplicationContext applicationContext;

    private static final String NACOS_PROTOCOL = "nacos";

    @Autowired
    private QpsBeanFactoryPostProcessor qpsBeanFactoryPostProcessor;

    @Value("${hsp.registry.address}")
    private String registryAddress;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("开始注入加密机公共服务");
        ReferenceBean<SignatureService> referenceBean = new ReferenceBean<>();
        referenceBean.setApplicationContext(applicationContext);
        referenceBean.setInterface(SignatureService.class);
        referenceBean.setCheck(false);
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(registryAddress);
        registryConfig.setProtocol(NACOS_PROTOCOL);
        referenceBean.setRegistry(registryConfig);
        referenceBean.afterPropertiesSet();
        qpsBeanFactoryPostProcessor.getConfigurableListableBeanFactory().registerSingleton(BeanUtils.getBeanNameWithImpl(SignatureService.class.getName()), referenceBean.get());
        logger.info("加密机公共服务注入完成");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
