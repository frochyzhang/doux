package com.allinfinance.dev.example.socket;

import com.allinfinance.dev.example.socket.factory.AppProcessFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/30 10:05
 */
@Configuration
public class GateApplicationListener implements ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GateApplicationListener.class);
    @Autowired
    private AppProcessFactory appProcessFactory;

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        appProcessFactory.cacheProcessors();
        logger.info("application closed!");
    }
}
