package com.allinfinance.dev.batch.tasklet.writer;

import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchJobExecution;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qipeng
 * @date 2022/1/26 0:16
 */
@Component
public class SimpleWriter implements ItemWriter<TblBatchJobExecution> {
    @Override
    public void write(List<? extends TblBatchJobExecution> list) throws Exception {
        System.out.println("写入文件成功！！！！");
    }
}
