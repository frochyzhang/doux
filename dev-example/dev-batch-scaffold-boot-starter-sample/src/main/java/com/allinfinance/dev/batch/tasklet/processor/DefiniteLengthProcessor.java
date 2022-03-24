package com.allinfinance.dev.batch.tasklet.processor;

import com.allinfinance.dev.batch.dto.DefiniteLengthDTO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/2/10 10:10
 */
@Component
public class DefiniteLengthProcessor implements ItemProcessor<DefiniteLengthDTO,DefiniteLengthDTO> {

    @Override
    public DefiniteLengthDTO process(DefiniteLengthDTO item) throws Exception {
        System.out.println(item.toString());
        return item;
    }
}
