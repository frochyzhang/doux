package com.allinfinance.dev.disruptor.processor;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2023/2/16 21:54
 */
public interface EventProcessor<T> {
    String processKey();

    void process(T data);
}
