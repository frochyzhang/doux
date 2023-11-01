package com.allinfinance.dev.hsp;

import com.allinfinance.dev.framework.conn.driver.Connection;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

/**
 * @author qipeng
 * @date 2023/1/13 9:56
 * @desc
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Long> queue = new LinkedBlockingQueue<>(20);
        IntStream.rangeClosed(1, 20)
                .forEach(i -> {
                    queue.add(Long.valueOf(i));
                });


        ExtensionLoader<Connection> extensionLoader = ExtensionLoaderFactory.getExtensionLoader(Connection.class);
        Connection hsp = extensionLoader.getExtension("hsp");

        Properties properties = new Properties();
        properties.put("serverIp", "10.250.1.17");
        properties.put("serverPort", "8888");
        properties.put("lengthField", "2");
        properties.put("bufferSize", "1024");
        properties.put("defaultNetworkTimeout", "3000");
        properties.put("connectTimeout", "1000");

        long x = System.currentTimeMillis();
        try {
            hsp.connect(properties);
        } finally {
            System.out.println(System.currentTimeMillis() - x);

        }

        System.out.println(System.currentTimeMillis());

        System.out.println();
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
