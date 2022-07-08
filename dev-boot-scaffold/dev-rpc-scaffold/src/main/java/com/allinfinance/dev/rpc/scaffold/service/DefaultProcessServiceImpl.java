package com.allinfinance.dev.rpc.scaffold.service;

import com.allinfinance.dev.framework.extension.annotation.Extension;
import com.allinfinance.dev.rpc.scaffold.api.AbstractProcessService;
import com.allinfinance.dev.rpc.scaffold.api.ProcessorKeyService;
import com.allinfinance.dev.rpc.scaffold.api.dto.AbstractResponseDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessRequestDTO;
import com.allinfinance.dev.rpc.scaffold.api.dto.ProcessResponseDTO;
import com.allinfinance.dev.rpc.scaffold.processor.BusinessProcessedFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/2/16 15:16
 */
@Extension("defaultProcessService")
public class DefaultProcessServiceImpl extends AbstractProcessService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultProcessServiceImpl.class);

    @Autowired
    private BusinessProcessedFactory businessProcessedFactory;

    @Autowired(required = false)
    private ProcessorKeyService processorKeyService;

    @Override
    public ProcessResponseDTO process(ProcessRequestDTO processRequestDTO) {
        if (processorKeyService == null) {
            throw new NullPointerException("未匹配到ProcessorKeyService实现!");
        }
        String processorKey = processorKeyService.getProcessorKey(processRequestDTO);
        logger.info("匹配到的processorKey: {}", processorKey);
        AbstractResponseDTO abstractResponseDTO = null;
        try {
            abstractResponseDTO = businessProcessedFactory.processed(processRequestDTO.getRequestDTO(), processorKey);
        } catch (Exception e) {
            logger.error("业务处理异常", e);
        }
        ProcessResponseDTO processResponseDTO = new ProcessResponseDTO(processRequestDTO.getRequestType());
        processResponseDTO.setResponseDTO(abstractResponseDTO);
        return processResponseDTO;
    }
}
