package com.allinfinance.dev.hsp;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * @author qipeng
 * @date 2022/6/17 16:10
 * @description
 */
public abstract class AbstractBenchmark {

    @Test
    public void execute() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("\\." + this.getClass().getSimpleName() + "\\.")
                .warmupIterations(3)
                .measurementIterations(3)
                .forks(0)
                .threads(100)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result("./result.json")
                .jvmArgs("-server")
                .build();

        new Runner(opt).run();
    }
}
