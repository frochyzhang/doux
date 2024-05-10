package cn.lezoo.doux.gateway.scaffold.processor;

import cn.hutool.extra.spring.SpringUtil;
import cn.lezoo.doux.gateway.scaffold.processor.api.BusinessProcessedFactory;
import cn.lezoo.doux.gateway.scaffold.processor.api.BusinessProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author huanghf
 * @date 2023/4/27 9:57
 */
@Component
public class BusinessProcessorLoader implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessProcessorLoader.class);

    @Autowired
    private BusinessProcessedFactory businessProcessedFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("Start loading business processor...");
        Arrays.stream(SpringUtil.getBeanNamesForType(BusinessProcessor.class))
                .forEach(processorBeanName -> businessProcessedFactory.register(SpringUtil.getBean(processorBeanName)));
        LOGGER.info("Business processor loading completed...");
    }
}
