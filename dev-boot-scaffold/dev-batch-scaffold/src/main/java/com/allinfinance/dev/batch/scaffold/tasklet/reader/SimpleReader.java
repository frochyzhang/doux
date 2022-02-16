package com.allinfinance.dev.batch.scaffold.tasklet.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/2/9 15:00
 */
@Component
public class SimpleReader implements ItemReader<String> {
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        //会一直读下去，有点问题
        return "读入字符串";
    }
}
