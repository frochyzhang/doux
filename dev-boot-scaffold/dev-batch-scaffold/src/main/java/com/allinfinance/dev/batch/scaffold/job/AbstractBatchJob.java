package com.allinfinance.dev.batch.scaffold.job;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/25 11:11
 */
public abstract class AbstractBatchJob {
    @Autowired
    protected JobBuilderFactory jobBuilderFactory;
    @Autowired
    protected StepBuilderFactory stepBuilderFactory;


}
