package com.allinfinance.dev.example.rabbitmq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Demo07Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void syncSend(String key, Object message) {
        // 创建 Demo07Message 消息
//        Demo07Message message = new Demo07Message();
//        message.setId(id);
        // 同步发送消息
//        rabbitTemplate.convertAndSend(Demo07Message.EXCHANGE, Demo07Message.ROUTING_KEY, message);
        rabbitTemplate.convertAndSend(key, message);
    }

}
