package com.allinfinance.dev.mrp.service.processor;

import com.allinfinance.dev.mrp.param.GenericReponse;
import com.allinfinance.dev.mrp.param.RequestParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:zhangyong@allinfinance.com">zhangyong</a>
 * @date 2021/8/9 14:07
 */
@Slf4j
@Component
public class MQServiceProcessor extends BaseBusinessProcessor {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public String supportType() {
        return RequestParams.MQ_SWITCH;
    }

    @Override
    public <T> GenericReponse<T> process(Object req, RequestParams requestParams) {
        GenericReponse<T> response = new GenericReponse<>(Boolean.TRUE);
        try {
            rabbitTemplate.convertAndSend(requestParams.getExchangeName(), requestParams.getRoutingKey(), req);
        } catch (Exception mqException) {
            log.error("调用MQ发布消息异常!", mqException);
            response.setRespStatus(Boolean.FALSE);
        }
        return response;
    }
}
