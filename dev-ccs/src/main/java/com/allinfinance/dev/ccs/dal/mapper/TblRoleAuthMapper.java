package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;

public interface TblRoleAuthMapper {
    int insert(TblRoleAuth record);

    int insertSelective(TblRoleAuth record);
}