package com.allinfinance.dev.rabbitmq.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zhangyong
 */
@Slf4j
@Service("confirmCallBackListener")
public class ConfirmCallBackListener implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("消息确认--:correlationData:{}, ack:{}, cause:{}", correlationData, ack, cause);
    }
}
