package com.allinfinance.dev.common.dictionary.desensitized.processor;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 23:15
 */
public abstract class BaseProcessor implements DesensitizedProcessor {
    @Autowired
    DesensitizedFactoryImpl desensitizedFactory;

    public BaseProcessor() {
    }

    @PostConstruct
    public void init() {
        this.desensitizedFactory.register(this);
    }
}
