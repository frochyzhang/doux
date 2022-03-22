package com.allinfinance.dev.batch.scaffold.listener;

import com.allinfinance.dev.batch.scaffold.config.BatchFileConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author qipeng
 * @date 2022/1/25 23:14
 */
@Component
public class FlatFileJobListener implements JobExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(FlatFileJobListener.class);
    @Autowired
    private BatchFileConfig batchFileConfig;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        logger.info("Job参数：" + jobParameters);
        Map<String, JobParameter> parameterMap = jobParameters.getParameters();
        JobParameter sourceFileName = parameterMap.get("sourceFileName");
        File file = new File(batchFileConfig.getSourceFilePath() + sourceFileName.getValue());
        if (!file.exists()) {
            logger.error("源文件不存在: {}", sourceFileName.getValue());
            throw new IllegalArgumentException("源文件不存在");
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            Map<String, JobParameter> parameterMap = jobExecution.getJobParameters().getParameters();
            JobParameter targetFileName = parameterMap.get("targetFileName");
            File file = new File(batchFileConfig.getTargetFilePath() + targetFileName.getValue());
            if (!file.exists()) {
                logger.error("目标文件未生成: {}", targetFileName.getValue());
                throw new IllegalArgumentException("目标文件未生成");
            }
            logger.info("Job执行成功，生成.OK文件...");
            try {
                FileUtils.touch(new File(batchFileConfig.getTargetFilePath() + targetFileName.getValue() + ".OK"));
            } catch (IOException e) {
                logger.error("生成.OK文件失败");
            }
        }
    }
}
