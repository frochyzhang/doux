package com.allinfinance.dev.hsp.util;

import cn.hutool.core.util.HexUtil;
import com.allinfinance.dev.connection.pool.scaffold.api.MessagePorter;
import com.allinfinance.dev.core.dto.hsp.constant.HashAlgorithmEnum;
import com.allinfinance.dev.hsp.constant.InstructionEnum;
import com.allinfinance.dev.hsp.constant.RespCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huanghf
 * @date 2022/6/24 14:14
 */
@Component
public class DigestUtil {
    private static final Logger logger = LoggerFactory.getLogger(DigestUtil.class);

    @Autowired
    private MessagePorter messagePorter;

    /**
     * 获取摘要--D30A
     *
     * @param hashAlgorithmEnum 摘要算法
     * @param data              做摘要的数据
     * @return 摘要，失败返回null
     */
    public String getDigest(HashAlgorithmEnum hashAlgorithmEnum, String data) {
        logger.info("开始组装摘要加密机指令");
        StringBuilder instruction = new StringBuilder();
        String dataHex = HexUtil.encodeHexStr(data);
        instruction.append(InstructionEnum.D30A.getCode())
                .append(hashAlgorithmEnum.getCode())
                .append(String.format("%04x", dataHex.length() / 2))
                .append(dataHex);

        logger.debug("请求加密机报文: {}", instruction.toString());
        String response = messagePorter.writeAndFlush(instruction.toString());
        logger.debug("加密机返回报文: {}", response);

        if (StringUtils.isBlank(response)) {
            logger.error("请求加密机获取摘要响应为空");
            return null;
        }

        int offset = 0;
        int div = 2;
        String respCode = response.substring(offset, div);
        logger.info("加密机返回报文应答码: {}", respCode);
        offset += div;
        if (RespCodeEnum.SUCCESS.getRespCode().equals(respCode)) {
            div = 4;
            String digestLengthHex = response.substring(offset, offset + div);
            logger.info("加密机返回成功，摘要数据长度: {}", digestLengthHex);
            offset += div;
            div = HexUtil.hexToInt(digestLengthHex) * 2;
            return response.substring(offset, offset + div);
        } else {
            logger.error("加密机返回失败，错误码: {}", response.substring(offset, offset + div));
            return null;
        }
    }
}
