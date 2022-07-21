package com.allinfinance.dev.connection.pool.scaffold.listener;

import com.allinfinance.dev.connection.pool.scaffold.api.MessagePorter;
import com.allinfinance.dev.connection.pool.scaffold.configure.ScaffoldConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangrn
 * @date 2022/7/19 17:25
 */
@ConditionalOnProperty(prefix = "com.allinfinance.connection.pool", name = "warmup", havingValue = "true")
@Component
public class ConnectionPoolStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private MessagePorter messagePorter;
    @Autowired
    private ScaffoldConfigure scaffoldConfigure;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        scaffoldConfigure.getServerMetadataMap().values()
                .forEach(config -> messagePorter.writeAndFlush(config.getPingQueryContent()));
    }
}
