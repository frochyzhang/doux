package com.allinfinance.dev.gateway.event;

import com.allinfinance.dev.rpc.scaffold.config.RpcConfigurationProperties;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/26 13:42
 */
public class ExporterClosedEvent extends ApplicationEvent {

    private final RpcConfigurationProperties.Bootstrap bootstrap;

    public ExporterClosedEvent(Object source, RpcConfigurationProperties.Bootstrap bootstrap) {
        super(source);
        this.bootstrap = bootstrap;
    }

    /**
     * The object on which the Event initially occurred.
     *
     * @return the object on which the Event initially occurred
     */
    @Override
    public Object getSource() {
        return super.getSource();
    }

    public RpcConfigurationProperties.Bootstrap getBootstrap() {
        return bootstrap;
    }
}
