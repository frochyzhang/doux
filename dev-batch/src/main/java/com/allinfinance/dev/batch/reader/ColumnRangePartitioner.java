package com.allinfinance.dev.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class ColumnRangePartitioner implements Partitioner {

    private Long startLine;
    private String filePath;
    private String encoding;

    private static final Logger logger = LoggerFactory.getLogger(ColumnRangePartitioner.class);

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Long endLine = getFileLineCount();
        Long targetSize = (endLine - startLine + 1) / gridSize;
        if (targetSize == 0) {
            targetSize = 1L;
        }
        logger.info("分片总数: {}, 每片处理记录数: {}, 记录总数: {}", gridSize, targetSize, endLine - startLine + 1);
        Map<String, ExecutionContext> result = new HashMap<>(16);
        int number = 0;
        Long start = startLine;
        Long end = start + targetSize - 1;
        while (start <= endLine) {
            ExecutionContext value = new ExecutionContext();
            result.put("Slaver" + number, value);
            if (end >= endLine) {
                end = endLine;
            }
            value.putLong("minValue", start - 1);
            value.putLong("maxValue", end);
            start += targetSize;
            end += targetSize;
            number++;
        }
        logger.info("共返回{}个分片", result.size());
        return result;
    }

    private Long getFileLineCount() {
        long lineCount = 0L;
        try (Stream<String> lines = Files.lines(Paths.get(filePath), Charset.forName(encoding))) {
            lineCount = lines.count();
        } catch (IOException e) {
            logger.error("文件行数获取失败!", e);
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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
