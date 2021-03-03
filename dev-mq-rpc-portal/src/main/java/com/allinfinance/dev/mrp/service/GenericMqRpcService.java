package com.allinfinance.dev.mrp.service;

import com.allinfinance.dev.core.loader.SpringConfigTool;
import com.allinfinance.dev.mrp.param.RequestParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author zhangyong
 */
@Slf4j
@Component
public class GenericMqRpcService<T, K> {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Map<String, Object> genericInvoke(K req, RequestParams params) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, Object> result = new HashMap<>();
        switch (params.getMrpSwitch()) {
            case RequestParams.MQ_SWITCH:
                rabbitTemplate.convertAndSend(params.getExchangeName(), params.getRoutingKey(), req);
                break;
            case RequestParams.RPC_SWITCH:
                /**
                 *
                 */
                try {
                    Object beanByClassName = SpringConfigTool.getBeanByClassName(params.getRpcInterface());
                    T invokeResp = (T) Class.forName(params.getRpcInterface())
                            .getDeclaredMethod(params.getRpcMethod(), req.getClass())
                            .invoke(beanByClassName, req);
                    result.put("rpcInvokeData", invokeResp);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error("rpc 调用失败!", e);
                    throw e;
                }
                break;
            default:
                log.error("开关配置有误：{}!", params.getMrpSwitch());
                throw new NoSuchElementException("开关配置有误!只允许为【MQ/RPC】");
        }
        result.put("status", Boolean.TRUE);
        return result;
    }
}
