package com.allinfinance.dev.gateway.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/3/26 13:42
 */
public class ExporterClosedEvent extends ApplicationEvent {

    private final String appUniqueId;

    public ExporterClosedEvent(Object source, String appUniqueId) {
        super(source);
        this.appUniqueId = appUniqueId;
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

    public String getAppUniqueId() {
        return appUniqueId;
    }
}
