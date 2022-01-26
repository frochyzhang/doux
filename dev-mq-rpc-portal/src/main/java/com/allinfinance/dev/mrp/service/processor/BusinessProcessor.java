package com.allinfinance.dev.mrp.service.processor;

import com.allinfinance.dev.mrp.param.GenericReponse;
import com.allinfinance.dev.mrp.param.RequestParams;

/**
 * @Author: qipeng
 * @Date: 2021/7/16
 **/

public interface BusinessProcessor {
    String supportType();

    <T> GenericReponse<T> process(Object req, RequestParams requestParams);
}
