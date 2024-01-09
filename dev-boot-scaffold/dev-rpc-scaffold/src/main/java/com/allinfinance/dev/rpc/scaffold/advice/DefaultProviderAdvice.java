package com.allinfinance.dev.rpc.scaffold.advice;

import com.allinfinance.dev.rpc.scaffold.advice.dto.SofaResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huanghf
 * @date 2023/3/22 15:01
 */
// @ProviderAdvice
public class DefaultProviderAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProviderAdvice.class);

    // @ProviderExceptionHandler(Exception.class)
    public SofaResponseDTO<Object> handleException(Exception exception) {
        LOGGER.error("服务提供方系统或业务异常", exception);
        return SofaResponseDTO.fail();
    }
}
