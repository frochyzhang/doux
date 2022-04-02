package com.allinfinance.dev.batch.config;

import com.allinfinance.dev.batch.dto.DefiniteLengthDTO;
import com.allinfinance.dev.batch.dto.DefiniteSeparatorDTO;
import com.allinfinance.dev.batch.scaffold.config.BatchFileConfig;
import com.allinfinance.dev.batch.scaffold.config.JobDtoMapperBeanFactory;
import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchJobExecution;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author qipeng
 * @description 配置了按固定分隔符读取、按固定长度读取
 * 以及数据库读取三种reader，可根据配置自行新增、修改
 * @date 2022/2/9 15:25
 */
@Configuration
@DependsOn({"applicationContextUtil", "jobDtoMapperBeanFactory"})
public class ItemReaderConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobDtoMapperBeanFactory jobDtoMapperBeanFactory;

    @Autowired
    private BatchFileConfig batchFileConfig;

    /**
     * 注册一个固定分割符的ItemReader
     *
     * @return FlatFileItemReader
     */

    @Bean("definiteSeparatorReader")
    public FlatFileItemReader<DefiniteSeparatorDTO> initReader1() {
        BatchFileConfig.FileConfig fileConfig = batchFileConfig.getFileConfigMap().get("cups-trx");
        FlatFileItemReader<DefiniteSeparatorDTO> itemReader = new FlatFileItemReader<>();
        itemReader.setEncoding(fileConfig.getEncoding());
        //如果输入资源不存在，阅读器会抛出异常。否则，它会记录问题并继续。
        itemReader.setStrict(true);
        itemReader.setLinesToSkip(fileConfig.getSkipLines());
        itemReader.setResource(new FileSystemResource(fileConfig.getSourceFilePath() + "definite-separator-source-file-2"));
        itemReader.setLineMapper(configDefaultSeparatorLineMapper());
        return itemReader;
    }

    /**
     * 注册一个固定长度的ItemReader
     *
     * @return FlatFileItemReader
     */
    @Bean("definiteLengthReader")
    public FlatFileItemReader<DefiniteLengthDTO> initReader2() {
        BatchFileConfig.FileConfig fileConfig = batchFileConfig.getFileConfigMap().get("cups-trx");
        FlatFileItemReader<DefiniteLengthDTO> itemReader = new FlatFileItemReader<>();
        itemReader.setEncoding(fileConfig.getEncoding());
        //如果输入资源不存在，阅读器会抛出异常。否则，它会记录问题并继续。
        itemReader.setStrict(true);
        itemReader.setLinesToSkip(fileConfig.getSkipLines());
        itemReader.setResource(new FileSystemResource(fileConfig.getSourceFilePath() + "definite-length-source-file"));
        Map<String, Range> tokenRangeMap = new LinkedHashMap<>();
        tokenRangeMap.put("ISIN", new Range(1, 12));
        tokenRangeMap.put("number", new Range(13, 15));
        tokenRangeMap.put("price", new Range(16, 20));
        tokenRangeMap.put("customer", new Range(21, 29));

        itemReader.setLineMapper(configDefaultLengthLineMapper(tokenRangeMap));
        return itemReader;
    }

    /**
     * 注册一个分页的数据库ItemReader
     *
     * @param queryProvider
     * @return
     */
    @Bean("batchJobExecutionPagingItemReader")
    public JdbcPagingItemReader<TblBatchJobExecution> itemReader3(PagingQueryProvider queryProvider) {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("JOB_EXECUTION_ID", 3);

        return new JdbcPagingItemReaderBuilder<TblBatchJobExecution>()
                .name("batchJobExecutionPagingItemReader")
                .dataSource(dataSource)
                .queryProvider(queryProvider)
                .parameterValues(parameterValues)
                .rowMapper(new TblBatchJobExecutionRowMapper())
                .pageSize(15)
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean queryProvider() {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();

        provider.setDataSource(dataSource);
        provider.setSelectClause("SELECT JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID, CREATE_TIME, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED, JOB_CONFIGURATION_LOCATION");
        provider.setFromClause("from batch_job_execution");
        provider.setWhereClause("where JOB_EXECUTION_ID>:JOB_EXECUTION_ID");
        provider.setSortKey("JOB_EXECUTION_ID");

        return provider;
    }

    protected static class TblBatchJobExecutionRowMapper implements RowMapper<TblBatchJobExecution> {

        @Override
        public TblBatchJobExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
            TblBatchJobExecution tblBatchJobExecution = new TblBatchJobExecution();
            tblBatchJobExecution.setJobExecutionId(rs.getLong(1));
            tblBatchJobExecution.setVersion(rs.getLong(2));
            tblBatchJobExecution.setJobInstanceId(rs.getLong(3));
            tblBatchJobExecution.setCreateTime(rs.getObject(4, LocalDateTime.class));
            tblBatchJobExecution.setStartTime(rs.getObject(5, LocalDateTime.class));
            tblBatchJobExecution.setEndTime(rs.getObject(6, LocalDateTime.class));
            tblBatchJobExecution.setStatus(rs.getString(7));
            tblBatchJobExecution.setExitCode(rs.getString(8));
            tblBatchJobExecution.setExitMessage(rs.getString(9));
            tblBatchJobExecution.setLastUpdated(rs.getObject(10, LocalDateTime.class));
            tblBatchJobExecution.setJobConfigurationLocation(rs.getString(11));

            return tblBatchJobExecution;
        }
    }

    /**
     * 配置按固定分割符映射的lineMapper
     *
     * @return LineMapper
     */
    private DefaultLineMapper<DefiniteSeparatorDTO> configDefaultSeparatorLineMapper() {
        BatchFileConfig.FileConfig fileConfig = batchFileConfig.getFileConfigMap().get("cups-trx");
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(fileConfig.getSeparator());
        //设置字段别名，增强可读性
        delimitedLineTokenizer.setNames("company", "year", "channel", "rank", "name", "count1", "count2");
        DefaultLineMapper<DefiniteSeparatorDTO> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(jobDtoMapperBeanFactory.getFieldSetMapper("definiteSeparatorDTOMapper", DefiniteSeparatorDTO.class));
        return defaultLineMapper;
    }

    /**
     * 配置按固定长度映射的lineMapper
     *
     * @return LineMapper
     */
    private DefaultLineMapper<DefiniteLengthDTO> configDefaultLengthLineMapper(Map<String, Range> tokenRangeMap) {
        FixedLengthTokenizer fixedLengthTokenizer = new FixedLengthTokenizer();
        //设置字段别名，增强可读性
        fixedLengthTokenizer.setNames(tokenRangeMap.keySet().toArray(new String[]{}));
        fixedLengthTokenizer.setStrict(true);
        fixedLengthTokenizer.setColumns(tokenRangeMap.values().toArray(new Range[]{}));
        DefaultLineMapper<DefiniteLengthDTO> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(fixedLengthTokenizer);
        defaultLineMapper.setFieldSetMapper(jobDtoMapperBeanFactory.getFieldSetMapper("definiteLengthDTOMapper", DefiniteLengthDTO.class));
        return defaultLineMapper;
    }
}
