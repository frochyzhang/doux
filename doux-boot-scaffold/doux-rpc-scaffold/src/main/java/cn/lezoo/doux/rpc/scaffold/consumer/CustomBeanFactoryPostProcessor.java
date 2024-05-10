package cn.lezoo.doux.rpc.scaffold.consumer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2021/12/30 15:33
 */
@Component
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        this.configurableListableBeanFactory = configurableListableBeanFactory;
    }

    public ConfigurableListableBeanFactory getConfigurableListableBeanFactory() {
        return configurableListableBeanFactory;
    }
}
