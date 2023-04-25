package com.allinfinance.dev.common.transmit.api;

import com.allinfinance.dev.common.transmit.api.dto.TransmitRequestDTO;
import com.allinfinance.dev.common.transmit.api.dto.TransmitResponseDTO;

/**
 * @author qipeng
 * @date 2022/9/9 15:45
 * @desc 文件传输服务接口
 */
public interface TransmitService {
    /**
     * 从本地上传文件到远程
     *
     * @param requestDTO 请求信息
     * @return 传输结果
     */
    TransmitResponseDTO upload(TransmitRequestDTO requestDTO);

    /**
     * 从远程下载文件到本地
     *
     * @param requestDTO 请求信息
     * @return 传输结果
     */
    TransmitResponseDTO download(TransmitRequestDTO requestDTO);

    /**
     * 从远程传输文件到远程
     *
     * @param requestDTO 请求信息
     * @return 传输结果
     */
    TransmitResponseDTO transmit(TransmitRequestDTO requestDTO);
}
