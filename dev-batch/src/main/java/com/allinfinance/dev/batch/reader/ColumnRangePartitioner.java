package com.allinfinance.dev.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class ColumnRangePartitioner implements Partitioner {

    private Long startLine;
    private String filePath;

    private static final Logger logger = LoggerFactory.getLogger(ColumnRangePartitioner.class);

    public Map<String, ExecutionContext> partition(int gridSize) {

        Long endLine = getFileLineCount();
        Long targetSize = (endLine - startLine + 1) / gridSize;
        if (targetSize == 0) {
            targetSize = 1L;
        }
        logger.info("OUR PARTITION SIZE WILL BE [" + targetSize + "]");
        logger.info("WE WILL HAVE [" + gridSize + "] PARTITIONS");
        Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
        int number = 0;
        Long start = startLine;
        Long end = start + targetSize - 1;
        while (start <= endLine) {
            ExecutionContext value = new ExecutionContext();
            result.put("Slaver" + number, value);
            if (end >= endLine) {
                end = endLine;
            }
            value.putLong("minValue", start);
            value.putLong("maxValue", end);
            start += targetSize;
            end += targetSize;
            number++;
        }
        logger.info("WE ARE RETURNING [" + result.size() + "] PARTITIONS");
        return result;
    }

    private Long getFileLineCount() {
        Long lineCount = 0L;
        try {
            lineCount = Files.lines(Paths.get(filePath)).count();
        } catch (IOException e) {
            logger.error("文件行数获取失败!");
        }
        return lineCount;
    }

    public Long getStartLine() {
        return startLine;
    }

    public void setStartLine(Long startLine) {
        this.startLine = startLine;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
