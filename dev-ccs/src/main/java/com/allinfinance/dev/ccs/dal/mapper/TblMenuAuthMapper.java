package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;

public interface TblMenuAuthMapper {
    int insert(TblMenuAuth record);

    int insertSelective(TblMenuAuth record);
}