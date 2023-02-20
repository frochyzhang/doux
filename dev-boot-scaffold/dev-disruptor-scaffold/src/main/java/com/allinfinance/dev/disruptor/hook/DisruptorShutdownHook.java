package com.allinfinance.dev.disruptor.hook;

import com.allinfinance.dev.disruptor.template.DisruptorEvent;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorShutdownHook extends Thread {

    private Disruptor<DisruptorEvent> disruptor;

    public DisruptorShutdownHook(Disruptor<DisruptorEvent> disruptor) {
        this.disruptor = disruptor;
    }

    @Override
    public void run() {
        disruptor.shutdown();
    }

}
