package com.allinfinance.dev.batch.scaffold.job;

import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchJobExecution;
import com.allinfinance.dev.batch.scaffold.dto.DefiniteLengthDTO;
import com.allinfinance.dev.batch.scaffold.dto.DefiniteSeparatorDTO;
import com.allinfinance.dev.batch.scaffold.listener.FlatFileJobListener;
import com.allinfinance.dev.batch.scaffold.tasklet.SimpleTasklet;
import com.allinfinance.dev.batch.scaffold.tasklet.processor.DefiniteLengthProcessor;
import com.allinfinance.dev.batch.scaffold.tasklet.processor.DefiniteSeparatorProcessor;
import com.allinfinance.dev.batch.scaffold.tasklet.processor.TblBatchExecutionProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
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
    private FlatFileJobListener jobListener;
    @Autowired
    private SimpleTasklet simpleTasklet;
    @Autowired
    private FlatFileItemReader definiteSeparatorReader;
    @Autowired
    private DefiniteSeparatorProcessor definiteSeparatorProcessor;
    @Autowired
    private FlatFileItemWriter definiteSeparatorWriter;
    @Autowired
    private FlatFileItemReader definiteLengthReader;
    @Autowired
    private DefiniteLengthProcessor definiteLengthProcessor;
    @Autowired
    private FlatFileItemWriter definiteLengthWriter;
    @Autowired
    private JdbcPagingItemReader batchJobExecutionPagingItemReader;
    @Autowired
    private TblBatchExecutionProcessor tblBatchExecutionProcessor;
    @Autowired
    private JdbcBatchItemWriter tblTestWriter;

    @Bean("testBatchJob")
    public Job job(@Qualifier("step1") Step step1, @Qualifier("step2") Step step2,
                   @Qualifier("step3") Step step3, @Qualifier("step4") Step step4) {
        return jobBuilderFactory.get("myJob")
                .listener(jobListener)
                .start(step1)
                .next(step2)
                .next(step3)
                .next(step4)
                .build();
    }

    @Bean
    protected Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(simpleTasklet)
                .build();
    }

    @Bean
    protected Step step2() {
        return stepBuilderFactory.get("step2")
                .<TblBatchJobExecution, TblBatchJobExecution>chunk(10)
                .reader(batchJobExecutionPagingItemReader)
                .processor(tblBatchExecutionProcessor)
                .writer(tblTestWriter)
                .build();
    }

    @Bean
    protected Step step3() {
        return stepBuilderFactory.get("step3")
                .<DefiniteSeparatorDTO, DefiniteSeparatorDTO>chunk(10)
                .reader(definiteSeparatorReader)
                .processor(definiteSeparatorProcessor)
                .writer(definiteSeparatorWriter)
                .build();
    }

    @Bean
    protected Step step4() {
        return stepBuilderFactory.get("step4")
                .<DefiniteLengthDTO, DefiniteLengthDTO>chunk(10)
                .reader(definiteLengthReader)
                .processor(definiteLengthProcessor)
                .writer(definiteLengthWriter)
                .build();
    }
}
