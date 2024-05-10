package cn.lezoo.doux.feign.scaffold;

import cn.lezoo.doux.feign.Request;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2023/10/18 18:59
 */
public class OptionsFactoryBean implements FactoryBean<Request.Options>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private String contextId;

    private Request.Options options;

    @Override
    public Class<?> getObjectType() {
        return Request.Options.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Request.Options getObject() throws Exception {
        if (options != null) {
            return options;
        }

        options = new Request.Options();
//        FeignClientProperties properties = applicationContext.getBean(FeignClientProperties.class);
//        options = createOptionsWithApplicableValues(properties.getConfig().get(properties.getDefaultConfig()), options);
//        options = createOptionsWithApplicableValues(properties.getConfig().get(contextId), options);
        return options;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

//    private Request.Options createOptionsWithApplicableValues(
//            FeignClientProperties.FeignClientConfiguration clientConfiguration, Request.Options options) {
//        if (Objects.isNull(clientConfiguration)) {
//            return options;
//        }
//
//        int connectTimeoutMillis = Objects.nonNull(clientConfiguration.getConnectTimeout())
//                ? clientConfiguration.getConnectTimeout() : options.connectTimeoutMillis();
//        int readTimeoutMillis = Objects.nonNull(clientConfiguration.getReadTimeout())
//                ? clientConfiguration.getReadTimeout() : options.readTimeoutMillis();
//        boolean followRedirects = Objects.nonNull(clientConfiguration.isFollowRedirects())
//                ? clientConfiguration.isFollowRedirects() : options.isFollowRedirects();
//        return new Request.Options(connectTimeoutMillis, TimeUnit.MILLISECONDS, readTimeoutMillis,
//                TimeUnit.MILLISECONDS, followRedirects);
//    }

}
