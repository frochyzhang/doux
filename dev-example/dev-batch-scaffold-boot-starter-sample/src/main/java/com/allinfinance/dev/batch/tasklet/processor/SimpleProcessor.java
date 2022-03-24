package com.allinfinance.dev.batch.tasklet.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/1/26 0:20
 */
@Component
public class SimpleProcessor implements ItemProcessor<String, String> {
    @Override
    public String process(String s) throws Exception {
        System.out.println("输入字符串：" + s);
        return "processor处理完成";
    }
}
