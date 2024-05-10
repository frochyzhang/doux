package cn.lezoo.doux.feign.scaffold;

import cn.hutool.extra.spring.SpringUtil;
import cn.lezoo.doux.feign.Client;
import cn.lezoo.doux.feign.ReflectiveFeign;
import cn.lezoo.doux.feign.Request;
import cn.lezoo.doux.feign.Target;
import cn.lezoo.doux.feign.codec.Decoder;
import cn.lezoo.doux.feign.codec.Encoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2023/10/18 17:41
 */
public class FeignClientFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware,
        BeanFactoryAware {

    private Class<?> type;

    private String name;

    private String url;

    private Integer msgLengthSize;

    private String msgEncode;

    private Integer timeout;

    private String contextId;

    private ApplicationContext applicationContext;

    private Client client = new Client.Default();
    private BeanFactory beanFactory;

    private Class<?> fallback = void.class;

    private Class<?> fallbackFactory = void.class;

    private int readTimeoutMillis = new Request.Options().readTimeoutMillis();

    private int connectTimeoutMillis = new Request.Options().connectTimeoutMillis();

    private boolean refreshableClient = false;

    private Class<Encoder> encoderClass;

    private Class<Decoder> decoderClass;

    public void afterPropertiesSet() {
        Assert.hasText(contextId, "Context id must be set");
        Assert.hasText(name, "Name must be set");
    }

    @Override
    public Object getObject() {
        return getTarget();
    }

    <T> T getTarget() {
        if (this.encoderClass != null) {
            this.client.setEncoder(SpringUtil.getBean(encoderClass));
        }
        if (this.decoderClass != null) {
            this.client.setDecoder(SpringUtil.getBean(decoderClass));
        }
        return (T) new ReflectiveFeign(this.client).newInstance(new Target.HardCodedTarget<>(type, name, url, msgEncode, timeout, msgLengthSize));
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
        beanFactory = context;
    }

    public Client getClient() {
        return client;
    }

    public FeignClientFactoryBean setClient(Client client) {
        this.client = client;
        return this;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public Class<?> getType() {
        return type;
    }

    public FeignClientFactoryBean setType(Class<?> type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public FeignClientFactoryBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public FeignClientFactoryBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getMsgLengthSize() {
        return msgLengthSize;
    }

    public FeignClientFactoryBean setMsgLengthSize(Integer msgLengthSize) {
        this.msgLengthSize = msgLengthSize;
        return this;
    }

    public String getMsgEncode() {
        return msgEncode;
    }

    public FeignClientFactoryBean setMsgEncode(String msgEncode) {
        this.msgEncode = msgEncode;
        return this;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public FeignClientFactoryBean setTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getContextId() {
        return contextId;
    }

    public FeignClientFactoryBean setContextId(String contextId) {
        this.contextId = contextId;
        return this;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public Class<?> getFallback() {
        return fallback;
    }

    public FeignClientFactoryBean setFallback(Class<?> fallback) {
        this.fallback = fallback;
        return this;
    }

    public Class<?> getFallbackFactory() {
        return fallbackFactory;
    }

    public FeignClientFactoryBean setFallbackFactory(Class<?> fallbackFactory) {
        this.fallbackFactory = fallbackFactory;
        return this;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public FeignClientFactoryBean setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
        return this;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public FeignClientFactoryBean setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }

    public boolean isRefreshableClient() {
        return refreshableClient;
    }

    public FeignClientFactoryBean setRefreshableClient(boolean refreshableClient) {
        this.refreshableClient = refreshableClient;
        return this;
    }

    public Class<Encoder> getEncoderClass() {
        return encoderClass;
    }

    public void setEncoderClass(Class<Encoder> encoderClass) {
        this.encoderClass = encoderClass;
    }

    public Class<Decoder> getDecoderClass() {
        return decoderClass;
    }

    public void setDecoderClass(Class<Decoder> decoderClass) {
        this.decoderClass = decoderClass;
    }
}
