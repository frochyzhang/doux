package cn.lezoo.doux.disruptor.hook;

import cn.lezoo.doux.disruptor.template.DisruptorEvent;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorShutdownHook extends Thread {

    private final Disruptor<DisruptorEvent> disruptor;

    public DisruptorShutdownHook(Disruptor<DisruptorEvent> disruptor) {
        this.disruptor = disruptor;
    }

    @Override
    public void run() {
        disruptor.shutdown();
    }

}
