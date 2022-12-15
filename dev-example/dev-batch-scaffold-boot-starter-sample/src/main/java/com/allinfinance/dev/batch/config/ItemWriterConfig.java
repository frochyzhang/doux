package com.allinfinance.dev.batch.config;

import com.allinfinance.dev.batch.dto.DefiniteLengthDTO;
import com.allinfinance.dev.batch.dto.DefiniteSeparatorDTO;
import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchJobExecution;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

/**
 * @author qipeng
 * @date 2022/2/10 10:43
 */
@Configuration
public class ItemWriterConfig {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private BatchFileConfig batchFileConfig;

    /**
     * 注册一个固定分隔符的文件ItemWriter
     *
     * @return FlatFileItemWriter
     */
    @Bean("definiteSeparatorWriter")
    public FlatFileItemWriter<DefiniteSeparatorDTO> itemWriter1() {
        BatchFileConfig.FileConfig fileConfig = batchFileConfig.getFileConfigMap().get("cups-trx");
        FileSystemResource outputResource = new FileSystemResource(fileConfig.getTargetFilePath() + "definite-separator-target-file");
        return new FlatFileItemWriterBuilder<DefiniteSeparatorDTO>()
                .name("definiteSeparatorWriter")
                .resource(outputResource)
                .delimited()
                .delimiter(fileConfig.getDelimiter())
                .names(new String[]{"company", "year", "channel", "rank", "name", "count1", "count2"})
                .build();
    }

    /**
     * 注册一个固定长度的文件ItemWriter
     *
     * @return FlatFileItemWriter
     */
    @Bean("definiteLengthWriter")
    public FlatFileItemWriter<DefiniteLengthDTO> itemWriter2() {
        BatchFileConfig.FileConfig fileConfig = batchFileConfig.getFileConfigMap().get("cups-trx");
        FileSystemResource outputResource = new FileSystemResource(fileConfig.getTargetFilePath() + "definite-length-target-file");
        return new FlatFileItemWriterBuilder<DefiniteLengthDTO>()
                .name("definiteLengthWriter")
                .resource(outputResource)
                .formatted()
                .format("%-18s%-6d%-9.2f%-10s")
                .names(new String[]{"ISIN", "number", "price", "customer"})
                .build();
    }

    /**
     * 注册数据库ItemWriter
     *
     * @return JdbcBatchItemWriter
     */
    @Bean("tblBatchJobExecutionWriter")
    public JdbcBatchItemWriter<TblBatchJobExecution> itemWriter3() {
        return new JdbcBatchItemWriterBuilder<TblBatchJobExecution>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into batch_job_execution (JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID,\n" +
                        "                                 CREATE_TIME, START_TIME, END_TIME,\n" +
                        "                                 STATUS, EXIT_CODE, EXIT_MESSAGE,\n" +
                        "                                 LAST_UPDATED, JOB_CONFIGURATION_LOCATION)\n" +
                        "values (:jobExecutionId, :version, :jobInstanceId, :createTime, :startTime, :endTime, :status, " +
                        ":exitCode, :exitMessage, :lastUpdated, :jobConfigurationLocation)")
                .build();
    }

    @Bean("tblTestWriter")
    public JdbcBatchItemWriter<TblBatchJobExecution> itemWriter4() {
        return new JdbcBatchItemWriterBuilder<TblBatchJobExecution>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into test1 (i) values (:jobExecutionId)")
                .build();
    }

}
