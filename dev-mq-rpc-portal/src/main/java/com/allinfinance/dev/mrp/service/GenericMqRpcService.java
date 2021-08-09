package com.allinfinance.dev.mrp.service;

import com.allinfinance.dev.mrp.param.GenericReponse;
import com.allinfinance.dev.mrp.param.RequestParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhangyong
 */
@Slf4j
@Component
public class GenericMqRpcService {

    @Value("${dev.mrp.switch}")
    private String mrpSwitch;

    @Autowired
    private BusinessHandlerFactory businessHandlerFactory;

    public <T> GenericReponse<T> genericInvoke(Object req, RequestParams params) {
        return businessHandlerFactory.processed(req, params, mrpSwitch);
    }
}
