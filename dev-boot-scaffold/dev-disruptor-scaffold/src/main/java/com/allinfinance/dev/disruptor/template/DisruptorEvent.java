package com.allinfinance.dev.disruptor.template;

import java.util.EventObject;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2023/2/16 21:16
 */
public class DisruptorEvent extends EventObject {
    private String eventKey;

    private Object data;

    public DisruptorEvent() {
        super(new Object());
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DisruptorEvent{" +
                "eventKey='" + eventKey + '\'' +
                ", data=" + data +
                '}';
    }
}
