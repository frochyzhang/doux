package cn.lezoo.doux.disruptor;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(DisruptorProperties.PREFIX)
public class DisruptorProperties {

    public static final String PREFIX = "cn.lezoo.doux.disruptor";

    /**
     * Enable Disruptor.
     */
    private boolean enabled = true;
    /**
     * 是否自动创建RingBuffer对象
     */
    private boolean ringBuffer = true;
    /**
     * RingBuffer缓冲区大小, 默认 1024
     */
    private int ringBufferSize = 1024;
    /**
     * 消息消费线程池大小, 默认 4
     */
    private int ringThreadNumbers = 4;
    /**
     * 是否对生产者，如果是则通过 RingBuffer.createMultiProducer创建一个多生产者的RingBuffer，否则通过RingBuffer.createSingleProducer创建一个单生产者的RingBuffer
     */
    private boolean multiProducer = false;

    /**
     * 消息出来责任链
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isRingBuffer() {
        return ringBuffer;
    }

    public void setRingBuffer(boolean ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public boolean isMultiProducer() {
        return multiProducer;
    }

    public void setMultiProducer(boolean multiProducer) {
        this.multiProducer = multiProducer;
    }

    public int getRingBufferSize() {
        return ringBufferSize;
    }

    public void setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }

    public int getRingThreadNumbers() {
        return ringThreadNumbers;
    }

    public void setRingThreadNumbers(int ringThreadNumbers) {
        this.ringThreadNumbers = ringThreadNumbers;
    }
}
