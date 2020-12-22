package com.allinfinance.dev.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author 张勇
 * @date 2020-11-28 01:24
 */
public class MinaApplication {

    private static final Logger logger = LoggerFactory.getLogger(MinaApplication.class);

    private final Set<Class<?>> primarySources;

    public MinaApplication(Class<?>... primarySources) {
        this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
    }

    public static void run(Class<?> primarySource, String... args) {
        run(new Class[]{primarySource}, args);
    }

    private static void run(Class<?>[] primarySources, String... args) {
        new MinaApplication(primarySources).run(args);
    }

    private void run(String... args) {
        Class<?> primarySource = new ArrayList<Class<?>>(primarySources).get(0);

        synchronized (primarySource) {
            while (true) {
                try {
                    primarySource.wait();
                } catch (InterruptedException e) {
                    logger.error("{} synchronized error:", primarySource.getName(), e);
                }
            }
        }
    }
}
