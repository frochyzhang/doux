package com.allinfinance.dev.batch.scaffold.job;

import com.allinfinance.dev.batch.scaffold.listener.SimpleJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author qipeng
 * @date 2022/1/24 15:11
 */
@Service
public class SimpleBatchJob extends AbstractBatchJob {
    @Autowired
    private SimpleJobListener jobListener;

    @Bean("testBatchJob")
    public Job job(@Qualifier("step1") Step step1, @Qualifier("step2") Step step2) {
        return jobBuilderFactory.get("myJob")
                .listener(jobListener)
                .start(step1)
                .next(step2)
                .build();
    }

    @Bean
    protected Step step1(ItemReader<String> reader,
                         ItemProcessor<String, String> processor,
                         ItemWriter<String> writer) {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    protected Step step2(Tasklet tasklet) {
        return stepBuilderFactory.get("step2")
                .tasklet(tasklet)
                .build();
    }

    @Bean
    protected ItemReader<String> reader() {
        System.out.println("reading ...");
        return () -> "read information";
    }

    @Bean
    protected ItemProcessor<String, String> processor() {
        System.out.println("processing ...");
        return input -> input + ", then process it";
    }

    @Bean
    protected ItemWriter<String> writer() {
        ItemWriter stringItemWriter = System.out::println;
        System.out.println("wrote");
        return stringItemWriter;
    }
}
