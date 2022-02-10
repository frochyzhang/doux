package com.allinfinance.dev.batch.scaffold.config;

import com.allinfinance.dev.batch.scaffold.dto.DefiniteLengthDTO;
import com.allinfinance.dev.batch.scaffold.dto.DefiniteSeparatorDTO;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * @author qipeng
 * @date 2022/2/10 10:43
 */
@Configuration
public class ItemWriterConfig {
    /**
     * 注册一个固定分隔符的文件ItemWriter
     *
     * @return FlatFileItemWriter
     */
    @Bean("definiteSeparatorWriter")
    @StepScope
    public FlatFileItemWriter<DefiniteSeparatorDTO> itemWriter1() {
        FileSystemResource outputResource = new FileSystemResource("D:\\project\\java\\dev\\dev-example\\dev-batch-scaffold-boot-starter-sample\\src\\main\\resources\\definite-separator-target-file");
        return new FlatFileItemWriterBuilder<DefiniteSeparatorDTO>()
                .name("customerCreditWriter")
                .resource(outputResource)
                .delimited()
                .delimiter("|")
                .names(new String[]{"company", "year", "channel", "rank", "name", "count1", "count2"})
                .build();
    }

    /**
     * 注册一个固定长度的文件ItemWriter
     *
     * @return FlatFileItemWriter
     */
    @Bean("definiteLengthWriter")
    @StepScope
    public FlatFileItemWriter<DefiniteLengthDTO> itemWriter2() {
        FileSystemResource outputResource = new FileSystemResource("D:\\project\\java\\dev\\dev-example\\dev-batch-scaffold-boot-starter-sample\\src\\main\\resources\\definite-length-target-file");
        return new FlatFileItemWriterBuilder<DefiniteLengthDTO>()
                .name("definiteLengthWriter")
                .resource(outputResource)
                .formatted()
                .format("%-18s%-6d%-9.2f%-10s")
                .names(new String[]{"ISIN", "number", "price", "customer"})
                .build();
    }
}
