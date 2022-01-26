package com.allinfinance.dev.mrp.service;


import com.allinfinance.dev.mrp.param.GenericReponse;
import com.allinfinance.dev.mrp.param.RequestParams;
import com.allinfinance.dev.mrp.service.processor.BusinessProcessor;

/**
 * @Author: qipeng
 * @Date: 2021/7/16
 **/

public interface BusinessHandlerFactory {
    void register(BusinessProcessor processor);

    <T> GenericReponse<T> processed(Object req, RequestParams requestParams, String supportType);
}
