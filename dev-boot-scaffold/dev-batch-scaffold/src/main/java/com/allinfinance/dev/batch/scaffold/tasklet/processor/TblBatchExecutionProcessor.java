package com.allinfinance.dev.batch.scaffold.tasklet.processor;

import com.allinfinance.dev.batch.scaffold.dal.model.TblBatchJobExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/2/10 16:37
 */
@Component
public class TblBatchExecutionProcessor implements ItemProcessor<TblBatchJobExecution, TblBatchJobExecution> {
    @Override
    public TblBatchJobExecution process(TblBatchJobExecution item) throws Exception {
        System.out.println("job编号：" + item.getJobExecutionId().toString() + "创建时间：" + item.getCreateTime());
        item.setJobExecutionId(item.getJobExecutionId() + 1000);
        return item;
    }
}
