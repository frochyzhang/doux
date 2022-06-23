package com.allinfinance.dev.hsp.service;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.core.dto.hsp.SummaryGetRequestDTO;
import com.allinfinance.dev.core.dto.hsp.SummaryGetResponseDTO;
import com.allinfinance.dev.core.util.hsp.ISummaryService;
import com.allinfinance.dev.hsp.constant.InstructionEnum;
import com.allinfinance.dev.hsp.constant.RespCodeEnum;
import com.allinfinance.dev.hsp.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author huanghf
 * @date 2022/6/21 14:28
 */
@Service
public class SummaryServiceImpl implements ISummaryService {
    private static final Logger logger = LoggerFactory.getLogger(SummaryServiceImpl.class);

    /**
     * 获取摘要
     *
     * @param summaryGetRequestDTO 摘要参数
     * @return 摘要
     */
    @Override
    public SummaryGetResponseDTO getSummary(SummaryGetRequestDTO summaryGetRequestDTO) {
        logger.info("开始组装摘要加密机指令");
        StringBuilder instruction = new StringBuilder();
        String dataHex = HexUtil.encodeHexStr(summaryGetRequestDTO.getData());
        instruction.append(InstructionEnum.D30A.getCode())
                .append(summaryGetRequestDTO.getHashAlgorithm().getCode())
                .append(StrUtil.getLengthStr(Integer.toHexString(dataHex.length() / 2), 4))
                .append(dataHex);

        logger.debug("请求加密机报文: {}", instruction.toString());
        // TODO: 2022/6/21 获取连接请求加密机
        String response = "";
        logger.debug("加密机返回报文: {}", response);

        int offset = 0;
        int div = 2;
        String respCode = response.substring(offset, div);
        logger.info("加密机返回报文应答码: {}", respCode);
        offset += div;
        if (RespCodeEnum.SUCCESS.getRespCode().equals(respCode)) {
            logger.info("加密机返回成功");
            div = 4;
            SummaryGetResponseDTO summaryGetResponseDTO = new SummaryGetResponseDTO(Boolean.TRUE);
            summaryGetResponseDTO.setSummaryData(HexUtil.decodeHexStr(response.substring(offset, offset + div)));
            return summaryGetResponseDTO;
        } else {
            logger.error("加密机返回失败，错误码: {}", response.substring(offset, offset + div));
            return new SummaryGetResponseDTO(Boolean.FALSE);
        }
    }
}
