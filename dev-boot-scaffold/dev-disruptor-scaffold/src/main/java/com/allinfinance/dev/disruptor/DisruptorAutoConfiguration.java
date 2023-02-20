package com.allinfinance.dev.disruptor;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.extra.spring.SpringUtil;
import com.allinfinance.dev.disruptor.hook.DisruptorShutdownHook;
import com.allinfinance.dev.disruptor.processor.EventProcessorFactory;
import com.allinfinance.dev.disruptor.template.DisruptorEvent;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@ComponentScan(basePackages = "com.allinfinance.dev.disruptor")
@Configuration
@EnableConfigurationProperties({DisruptorProperties.class})
public class DisruptorAutoConfiguration {


    private static final Logger logger = LoggerFactory.getLogger(DisruptorAutoConfiguration.class);

    /**
     * 决定一个消费者将如何等待生产者将Event置入Disruptor的策略。用来权衡当生产者无法将新的事件放进RingBuffer时的处理策略。
     * （例如：当生产者太快，消费者太慢，会导致生成者获取不到新的事件槽来插入新事件，则会根据该策略进行处理，默认会堵塞）
     *
     * @return {@link WaitStrategy} instance
     */
    @Bean
    public WaitStrategy waitStrategy() {
        return new YieldingWaitStrategy();
    }

    @Bean
    public ThreadFactory threadFactory() {
        return new NamedThreadFactory("event-thread-", false);
    }

    @Bean
    public EventFactory<DisruptorEvent> eventFactory() {
        return DisruptorEvent::new;
    }

    /**
     * <p>
     * 创建Disruptor
     * </p>
     * <p>
     * 1 eventFactory 为
     * <p>
     * 2 ringBufferSize为RingBuffer缓冲区大小，最好是2的指数倍
     * </p>
     *
     * @param properties    : 配置参数
     * @param waitStrategy  : 一种策略，用来均衡数据生产者和消费者之间的处理效率，默认提供了3个实现类
     * @param threadFactory : 线程工厂
     * @param eventFactory  : 工厂类对象，用于创建一个个的LongEvent， LongEvent是实际的消费数据，初始化启动Disruptor的时候，Disruptor会调用该工厂方法创建一个个的消费数据实例存放到RingBuffer缓冲区里面去，创建的对象个数为ringBufferSize指定的
     * @return {@link Disruptor} instance
     */
    @Bean
    public Disruptor<DisruptorEvent> disruptor(
            DisruptorProperties properties,
            WaitStrategy waitStrategy,
            ThreadFactory threadFactory,
            EventFactory<DisruptorEvent> eventFactory) {

        // http://blog.csdn.net/a314368439/article/details/72642653?utm_source=itdadao&utm_medium=referral

        Disruptor<DisruptorEvent> disruptor = null;
        if (properties.isMultiProducer()) {
            disruptor = new Disruptor<>(eventFactory, properties.getRingBufferSize(), threadFactory,
                    ProducerType.MULTI, waitStrategy);
        } else {
            disruptor = new Disruptor<>(eventFactory, properties.getRingBufferSize(), threadFactory,
                    ProducerType.SINGLE, waitStrategy);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(properties.getRingThreadNumbers(), threadFactory);
        // 使用disruptor创建消费者组
        disruptor.handleEventsWith((customEvent, l, b) -> {
            executorService.submit(() -> {
                logger.info(String.valueOf(customEvent));
                SpringUtil.getBean(EventProcessorFactory.class).process(customEvent);
            });
        });

        // 启动
        disruptor.start();

        /*
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
         * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
         */
        Runtime.getRuntime().addShutdownHook(new DisruptorShutdownHook(disruptor));

        return disruptor;

    }

    /**
     * <p>
     * 创建RingBuffer
     * </p>
     * <p>
     * 1 eventFactory 为
     * <p>
     * 2 ringBufferSize为RingBuffer缓冲区大小，最好是2的指数倍
     * </p>
     *
     * @param properties   ：配置参数
     * @param waitStrategy ： 一种策略，用来均衡数据生产者和消费者之间的处理效率，默认提供了3个实现类
     * @param eventFactory ：  工厂类对象，用于创建一个个的LongEvent， LongEvent是实际的消费数据，初始化启动Disruptor的时候，Disruptor会调用该工厂方法创建一个个的消费数据实例存放到RingBuffer缓冲区里面去，创建的对象个数为ringBufferSize指定的
     * @return {@link RingBuffer} instance
     */
    @Bean
    public RingBuffer<DisruptorEvent> ringBuffer(DisruptorProperties properties, WaitStrategy waitStrategy,
                                                 EventFactory<DisruptorEvent> eventFactory) {
        // http://blog.csdn.net/a314368439/article/details/72642653?utm_source=itdadao&utm_medium=referral
        /*
         * 第一个参数叫EventFactory，从名字上理解就是“事件工厂”，其实它的职责就是产生数据填充RingBuffer的区块。
         * 第二个参数是RingBuffer的大小，它必须是2的指数倍 目的是为了将求模运算转为&运算提高效率
         * 第三个参数是RingBuffer的生产都在没有可用区块的时候(可能是消费者（或者说是事件处理器） 太慢了)的等待策略
         */
        RingBuffer<DisruptorEvent> ringBuffer = null;
        if (properties.isMultiProducer()) {
            // RingBuffer.createMultiProducer创建一个多生产者的RingBuffer
            ringBuffer = RingBuffer.createMultiProducer(eventFactory, properties.getRingBufferSize(), waitStrategy);
        } else {
            // RingBuffer.createSingleProducer创建一个单生产者的RingBuffer
            ringBuffer = RingBuffer.createSingleProducer(eventFactory, properties.getRingBufferSize(), waitStrategy);
        }

        return ringBuffer;
    }

    @Bean
    public EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> oneArgEventTranslator() {
        return (customEvent, l, event) -> {
            customEvent.setEventKey(event.getEventKey());
            customEvent.setData(event.getData());
        };
    }
}


