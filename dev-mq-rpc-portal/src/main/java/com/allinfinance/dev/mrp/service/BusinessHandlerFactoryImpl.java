package com.allinfinance.dev.mrp.service;

import com.allinfinance.dev.mrp.param.GenericReponse;
import com.allinfinance.dev.mrp.param.RequestParams;
import com.allinfinance.dev.mrp.service.processor.BusinessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: qipeng
 * @Date: 2021/7/16
 **/
@Slf4j
@Component
public class BusinessHandlerFactoryImpl implements BusinessHandlerFactory {

    private static final Map<String, BusinessProcessor> PROCESSOR_MAP = new ConcurrentHashMap<>();

    @Override
    public void register(BusinessProcessor processor) {
        PROCESSOR_MAP.put(processor.supportType(), processor);
    }

    @Override
    public <T> GenericReponse<T> processed(Object req, RequestParams requestParams, String supportType) {
        BusinessProcessor businessProcessor = PROCESSOR_MAP.get(supportType);
        if (businessProcessor == null) {
            log.error("开关配置有误：{}!", supportType);
            throw new NoSuchElementException("开关配置有误!只允许为【MQ/RPC】");
        }
        return businessProcessor.<T>process(req, requestParams);
    }
}
