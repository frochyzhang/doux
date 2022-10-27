package com.allinfinance.dev.tools.hsp;

import com.allinfinance.dev.common.hsp.api.dto.SignatureGetBySM2PrivateKeyRequestDTO;
import com.allinfinance.dev.common.hsp.api.dto.SignatureVerifyBySM2PublicKeyRequestDTO;
import com.allinfinance.dev.tools.hsp.config.HspConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qipeng
 * @date 2022/10/27 11:21
 * @desc
 */
@Mapper
public interface DTOMapper {
    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    SignatureGetBySM2PrivateKeyRequestDTO convertSignDTO(HspConfig.SignConfig signConfig);

    SignatureVerifyBySM2PublicKeyRequestDTO convertVerifyDTO(HspConfig.VerifyConfig verifyConfig);
}
