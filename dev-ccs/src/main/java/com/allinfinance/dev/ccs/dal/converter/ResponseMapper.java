package com.allinfinance.dev.ccs.dal.converter;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dto.AuthsQueryResponseDTO;
import com.allinfinance.dev.ccs.dto.PageableRolesQueryResponseDTO;
import com.allinfinance.dev.ccs.dto.RolesQueryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author huanghf
 * @date 2021/12/23 15:54
 */
@Mapper
public interface ResponseMapper {
    ResponseMapper INSTANCE = Mappers.getMapper(ResponseMapper.class);

    PageableRolesQueryResponseDTO convertToPageableRolesQueryResponseDTO(TblRole tblRole, List<String> authIdList);

    RolesQueryResponseDTO convertToRolesQueryResponseDTO(TblRole tblRole);

    AuthsQueryResponseDTO convertToAuthsQueryResponseDTO(TblAuth tblAuth);
}
