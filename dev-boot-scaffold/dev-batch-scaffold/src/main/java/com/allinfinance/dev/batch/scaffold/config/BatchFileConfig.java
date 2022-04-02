package com.allinfinance.dev.batch.scaffold.config;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * @author qipeng
 * @date 2022/3/22 10:36
 */
@Configuration
@ConfigurationProperties(prefix = "com.allinfinance.batch")
public class BatchFileConfig implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(BatchFileConfig.class);

    /**
     * 是否开启文件路径验证
     */
    private Boolean verifyEnabled;
    /**
     * 文件配置
     */
    private Map<String, FileConfig> fileConfigMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        Iterator<Map.Entry<String, FileConfig>> iterator = this.fileConfigMap.entrySet().iterator();
        //文件路径校验
        if (!ObjectUtils.isEmpty(this.verifyEnabled) && this.verifyEnabled) {
            while (iterator.hasNext()) {
                FileConfig fileConfig = iterator.next().getValue();
                Assert.notNull(fileConfig.getSourceFilePath(), "批量源文件路径不能为空！");
                Assert.notNull(fileConfig.getTargetFilePath(), "批量目标文件路径不能为空！");
                File sourcePath = new File(fileConfig.getSourceFilePath());
                File targetPath = new File(fileConfig.getTargetFilePath());
                if (!sourcePath.exists() || !sourcePath.isDirectory()) {
                    logger.error("源文件路径有误：{}", fileConfig.getSourceFilePath());
                    throw new RuntimeException("源文件路径有误");
                } else if (!targetPath.exists() || !targetPath.isDirectory()) {
                    logger.error("目标文件路径有误：{}", fileConfig.getTargetFilePath());
                    throw new RuntimeException("目标文件路径有误");
                }
                //文件跳行校验，为空或为负数时置为0
                if (ObjectUtils.isEmpty(fileConfig.getSkipLines()) || fileConfig.getSkipLines() < 0) {
                    logger.info("参数[skipLines]有误，已重置为0！");
                    fileConfig.setSkipLines(0);
                }
            }
        }

        logger.info("批量文件读写参数：{}", this);
    }

    public Boolean getVerifyEnabled() {
        return verifyEnabled;
    }

    public void setVerifyEnabled(Boolean verifyEnabled) {
        this.verifyEnabled = verifyEnabled;
    }

    public Map<String, FileConfig> getFileConfigMap() {
        return fileConfigMap;
    }

    public void setFileConfigMap(Map<String, FileConfig> fileConfigMap) {
        this.fileConfigMap = fileConfigMap;
    }

    @Override
    public String toString() {
        return "BatchFileConfig{" +
                "verifyEnabled=" + verifyEnabled +
                ", fileConfigMap=" + fileConfigMap +
                '}';
    }

    public static class FileConfig {
        /**
         * 批量源文件读取路径
         */
        private String sourceFilePath;
        /**
         * 批量目标文件路径
         */
        private String targetFilePath;
        /**
         * 文件读取分隔符，默认用分号分割
         */
        private String separator = ";";
        /**
         * 文件写分隔符，默认用竖线分割
         */
        private String delimiter = "|";
        /**
         * 文件编码类型，默认为UTF-8
         */
        private String encoding = "UFT-8";
        /**
         * 跳过文件内容前n行
         */
        private Integer skipLines;

        public String getSourceFilePath() {
            return sourceFilePath;
        }

        public void setSourceFilePath(String sourceFilePath) {
            this.sourceFilePath = sourceFilePath;
        }

        public String getTargetFilePath() {
            return targetFilePath;
        }

        public void setTargetFilePath(String targetFilePath) {
            this.targetFilePath = targetFilePath;
        }

        public String getSeparator() {
            return separator;
        }

        public void setSeparator(String separator) {
            this.separator = separator;
        }

        public String getDelimiter() {
            return delimiter;
        }

        public void setDelimiter(String delimiter) {
            this.delimiter = delimiter;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public Integer getSkipLines() {
            return skipLines;
        }

        public void setSkipLines(Integer skipLines) {
            this.skipLines = skipLines;
        }

        @Override
        public String toString() {
            return "FileConfig{" +
                    "sourceFilePath='" + sourceFilePath + '\'' +
                    ", targetFilePath='" + targetFilePath + '\'' +
                    ", separator='" + separator + '\'' +
                    ", delimiter='" + delimiter + '\'' +
                    ", encoding='" + encoding + '\'' +
                    ", skipLines=" + skipLines +
                    '}';
        }
    }
}
