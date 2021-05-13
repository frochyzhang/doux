package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblAuth;

public interface TblAuthMapper {
    int deleteByPrimaryKey(String authId);

    int insert(TblAuth record);

    int insertSelective(TblAuth record);

    TblAuth selectByPrimaryKey(String authId);

    int updateByPrimaryKeySelective(TblAuth record);

    int updateByPrimaryKey(TblAuth record);
}