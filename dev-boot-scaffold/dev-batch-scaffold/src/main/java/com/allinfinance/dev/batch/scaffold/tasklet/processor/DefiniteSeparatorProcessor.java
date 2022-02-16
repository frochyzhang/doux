package com.allinfinance.dev.batch.scaffold.tasklet.processor;

import com.allinfinance.dev.batch.scaffold.dto.DefiniteSeparatorDTO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/2/9 17:04
 */
@Component
public class DefiniteSeparatorProcessor implements ItemProcessor<DefiniteSeparatorDTO, DefiniteSeparatorDTO> {
    @Override
    public DefiniteSeparatorDTO process(DefiniteSeparatorDTO definiteSeparatorDTO) throws Exception {
        System.out.println(definiteSeparatorDTO.toString());
        return definiteSeparatorDTO;
    }
}
