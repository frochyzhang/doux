package com.allinfinance.dev.batch.scaffold.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @description:
 * @date 2022/1/25 23:40
 */
@Component
public class SimpleTasklet implements Tasklet {
    /**
     * Given the current context in the form of a step contribution, do whatever
     * is necessary to process this unit inside a transaction. Implementations
     * return {@link RepeatStatus#FINISHED} if finished. If not they return
     * {@link RepeatStatus#CONTINUABLE}. On failure throws an exception.
     *
     * @param contribution mutable state to be passed back to update the current
     *                     step execution
     * @param chunkContext attributes shared between invocations but not between
     *                     restarts
     * @return an {@link RepeatStatus} indicating whether processing is
     * continuable. Returning {@code null} is interpreted as {@link RepeatStatus#FINISHED}
     * @throws Exception thrown if error occurs during execution.
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("tasklet done successfully!");
        return RepeatStatus.FINISHED;
    }
}
