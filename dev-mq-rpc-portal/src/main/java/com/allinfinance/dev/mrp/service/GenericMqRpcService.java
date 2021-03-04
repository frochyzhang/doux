package com.allinfinance.dev.mrp.service;

import com.allinfinance.dev.core.loader.SpringConfigTool;
import com.allinfinance.dev.mrp.param.GenericReponse;
import com.allinfinance.dev.mrp.param.RequestParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

/**
 * @author zhangyong
 */
@Slf4j
@Component
public class GenericMqRpcService<T, K> {

    @Value("${dev.mrp.switch}")
    private String mrpSwitch;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public GenericReponse<T> genericInvoke(K req, RequestParams params) {
        GenericReponse<T> response = new GenericReponse<>(Boolean.TRUE);
        switch (mrpSwitch) {
            case RequestParams.MQ_SWITCH:
                try {
                    rabbitTemplate.convertAndSend(params.getExchangeName(), params.getRoutingKey(), req);
                } catch (Exception mqException) {
                    log.error("调用MQ发布消息异常!", mqException);
                    response.setRespStatus(Boolean.FALSE);
                }
                break;
            case RequestParams.RPC_SWITCH:
                /**
                 * 1、根据接口类获取refference bean
                 * 2、根据rpc方法，请求类型，请求体反射调用
                 */
                try {
                    Object beanByClassName = SpringConfigTool.getBeanByClassName(params.getRpcInterface());
                    T invokeResp = (T) Class.forName(params.getRpcInterface())
                            .getDeclaredMethod(params.getRpcMethod(), req.getClass())
                            .invoke(beanByClassName, req);
                    response.setRpcInvokeData(invokeResp);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error("rpc 调用失败!", e);
                    response.setRespStatus(Boolean.FALSE);
                }
                break;
            default:
                log.error("开关配置有误：{}!", mrpSwitch);
                throw new NoSuchElementException("开关配置有误!只允许为【MQ/RPC】");
        }
        return response;
    }
}
