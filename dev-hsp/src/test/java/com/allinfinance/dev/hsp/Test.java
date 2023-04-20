package com.allinfinance.dev.hsp;

import cn.hutool.core.date.StopWatch;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qipeng
 * @date 2023/1/13 9:56
 * @desc
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
//        HashSet<Long> set = new HashSet<>();
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start("System.nanoTime()");
//        CountDownLatch latch1 = new CountDownLatch(500000);
//        for (int i = 0; i < 500000; i++) {
//            new Thread(() -> {
//                set.add(System.nanoTime());
//                latch1.countDown();
//                System.out.println("latch1 " + latch1.getCount());
//            }).start();
//        }
//        latch1.await();
//        stopWatch.stop();
//
////        stopWatch.start("AtomicLong");
////        CountDownLatch latch2 = new CountDownLatch(500000);
////        AtomicLong atomicLong = new AtomicLong(0);
////        for (int i = 0; i < 500000; i++) {
////            new Thread(() -> {
////                set.add(atomicLong.addAndGet(1));
////                latch2.countDown();
////                System.out.println("latch2 " + latch2.getCount());
////            }).start();
////        }
////        latch2.await();
////        stopWatch.stop();
//        System.out.println(stopWatch.prettyPrint());
//        System.out.println(set.size());
    }


    public double angleClock(int hour, int minutes) {
        BigDecimal hourUnit = BigDecimal.valueOf(30);
        BigDecimal minutesUnit = BigDecimal.valueOf(6);

        if (hour == 12) {
            hour = 0;
        }
        BigDecimal minutesWrapper = BigDecimal.valueOf(minutes);
        BigDecimal hourWrapper = BigDecimal.valueOf(hour);
        BigDecimal minutesAngle = minutesUnit.multiply(minutesWrapper);
        BigDecimal hourOffset = hourWrapper.add(minutesWrapper.divide(BigDecimal.valueOf(60)));
        BigDecimal hourAngle = hourUnit.multiply(hourOffset);
        BigDecimal originResult = hourAngle.subtract(minutesAngle).abs();

        if (originResult.compareTo(BigDecimal.valueOf(180)) >= 0) {
            return 360 - originResult.doubleValue();
        } else {
            return originResult.doubleValue();
        }
    }
}
