package com.allinfinance.dev.ccp.service;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author qipeng
 * @date 2022/6/17 16:10
 * @description
 */
public abstract class AbstractBenchmark {
    /**
     * 现在在跑的是benchmark，也就是基准测试，主要用于测试当前服务的tps，平均响应时间等
     * @throws RunnerException
     */
    @Test
    public void execute() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("\\." + this.getClass().getSimpleName() + "\\.")
                .warmupIterations(3)
                .measurementIterations(3)
                .forks(0)
                .threads(8)
                .shouldDoGC(true)
                .shouldFailOnError(false)
                .resultFormat(ResultFormatType.JSON)
                .result("./result.json")
                .jvmArgs("-server")
                .build();

        new Runner(opt).run();
    }
}
