package com.allinfinance.dev.mrp.service.processor;

import com.allinfinance.dev.mrp.service.BusinessHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2021/7/16
 **/
public abstract class BaseBusinessProcessor implements BusinessProcessor {
    @Autowired
    private BusinessHandlerFactory factory;

    @PostConstruct
    public void init() {
        factory.register(this);
    }
}
