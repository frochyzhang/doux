package com.allinfinance.dev.rpc.scaffold.processor;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/16 16:55
 */
public abstract class AbstractBusinessProcessor implements BusinessProcessor {
    @Autowired
    private BusinessProcessedFactory businessProcessedFactory;

    @PostConstruct
    public void init() {
        businessProcessedFactory.register(this);
    }
}
