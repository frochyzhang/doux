package com.allinfinance.dev.batch.task;

import com.allinfinance.dev.batch.basic.IBasicBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 张勇
 * @description
 * @date 2020/12/10 15:18
 */
public class FileWriter implements ItemWriter<TestStrDto> {

    private static int i  = 0;

    @Autowired
    private IBasicBatchService basicBatchService;

    private static final Logger logger = LoggerFactory.getLogger(FileWriter.class);
    @Override
    public void write(List<? extends TestStrDto> list) throws Exception {
        if (list == null || list.isEmpty()) {
            logger.info("插入数据为空!");
            return;
        }
        logger.info("插入数据数:" + list.size());

        for (TestStrDto testStrDto : list) {
            logger.info("{}",testStrDto);
        }
//        i+=list.size();
//        if (i==600){
//            basicBatchService.pauseBatch("readFileJob");
//        }
    }
}
