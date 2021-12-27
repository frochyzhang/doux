package com.allinfinance.dev.ccs.dal.converter;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dto.AuthCreateRequestDTO;
import com.allinfinance.dev.ccs.dto.AuthInfoUpdateRequestDTO;
import com.allinfinance.dev.ccs.dto.RoleCreateRequestDTO;
import com.allinfinance.dev.ccs.dto.RoleInfoUpdateRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;

/**
 * @author huanghf
 * @date 2021/12/24 10:38
 */
@Mapper
public interface PoMapper {
    PoMapper INSTANCE = Mappers.getMapper(PoMapper.class);

    TblRoleAuth convertToTblRoleAuth(String roleId, String authId);

    TblRole convertToTblRole(RoleInfoUpdateRequestDTO roleInfoUpdateRequestDTO, Date updateTime, String updateBy);

    TblRole convertToTblRole(RoleCreateRequestDTO roleCreateRequestDTO, Date createTime, String createBy);

    TblMenuAuth convertToTblMenuAuth(String authId, String menuId);

    TblAuth convertToTblAuth(AuthInfoUpdateRequestDTO authInfoUpdateRequestDTO, Date updateTime, String updateBy);

    TblAuth convertToTblAuth(AuthCreateRequestDTO authCreateRequestDTO, Date createTime, String createBy);
}
