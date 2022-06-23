package com.yuyee.dev.connection.scaffold.pool;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.HexUtil;
import com.yuyee.dev.connection.scaffold.ConnectionPoolAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author qipeng
 * @date 2022/6/17 16:17
 * @description
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
@SpringBootTest(classes = ConnectionPoolAutoConfiguration.class)
public class NettyClientBenchmark extends AbstractBenchmark {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientBenchmark.class);
    private static PoolManager poolManager;
    private static QueueManger queueManger;


    @Autowired
    private void setPoolManager(PoolManager poolManager, QueueManger queueManger) {
        NettyClientBenchmark.poolManager = poolManager;
        NettyClientBenchmark.queueManger = queueManger;
    }

//    @Benchmark
//    public void poolTest() throws Exception {
//        //发送请求
//        String msg = HexUtil.encodeHexStr(UUID.fastUUID().toString());
//        String s = queueManger.writeAndFlush(msg);
//        Assertions.assertEquals(msg, s);
//        logger.info(s);
//    }

    @Benchmark
    public void queueTest() {
        //发送请求
        String msg = HexUtil.encodeHexStr(UUID.fastUUID().toString());
        String s = queueManger.writeAndFlush(msg);
        Assertions.assertEquals(msg, s);
        logger.info(s);
    }
//
//    @Test
//    public void test_once() {
//        for (int i = 0; i < 100; i++) {
//            logger.info(poolManager.writeAndFlush("3132333435"));
//        }
//    }
//
//    @Test
//    public void test_multiThread() throws InterruptedException {
//        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("yuyee-", false);
//
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024),
//                namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
//
//        int LOOP_COUNT = 100;
//
//        CountDownLatch latch = new CountDownLatch(LOOP_COUNT);
//        for (int i = 0; i < LOOP_COUNT; i++) {
//            executor.submit(() -> {
//                logger.info(poolManager.writeAndFlush("3132333435"));
//                latch.countDown();
//            });
//        }
//
//        latch.await();
//        executor.shutdown();
//    }
}
