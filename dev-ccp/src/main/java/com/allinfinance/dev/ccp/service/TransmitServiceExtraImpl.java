package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.common.api.transmit.TransmitService;
import com.allinfinance.dev.common.api.transmit.dto.TransmitRequestDTO;
import com.allinfinance.dev.common.api.transmit.dto.TransmitResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author qipeng
 * @date 2022/9/9 16:06
 * @desc
 */
public class TransmitServiceExtraImpl implements TransmitService {
    private static final Logger logger = LoggerFactory.getLogger(TransmitServiceExtraImpl.class);

    @Value("${dev.ccp.file.local-path" +
            "}")
    private String localPath;

    /**
     * 从本地上传文件到远程
     *
     * @param requestDTO 请求信息
     * @return 传输结果
     */
    @Override
    public TransmitResponseDTO upload(TransmitRequestDTO requestDTO) {
        return null;
    }

    /**
     * 从远程下载文件到本地
     *
     * @param requestDTO 请求信息
     * @return 传输结果
     */
    @Override
    public TransmitResponseDTO download(TransmitRequestDTO requestDTO) {
        return null;
    }

    /**
     * 从远程传输文件到远程
     *
     * @param requestDTO 请求信息
     * @return 传输结果
     */
    @Override
    public TransmitResponseDTO transmit(TransmitRequestDTO requestDTO) {
        return null;
    }
}
