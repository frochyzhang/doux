package com.allinfinance.dev.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/1/25 23:40
 */
@Component
public class SimpleTasklet implements Tasklet {
    private static final Logger logger = LoggerFactory.getLogger(SimpleTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("simpleTasklet done successfully!");
        return RepeatStatus.FINISHED;
    }
}
