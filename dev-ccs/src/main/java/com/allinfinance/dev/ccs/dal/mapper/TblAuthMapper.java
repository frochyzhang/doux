package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblAuth;

import java.util.List;

public interface TblAuthMapper {
    int deleteByPrimaryKey(Integer authId);

    int insert(TblAuth record);

    int insertSelective(TblAuth record);

    TblAuth selectByPrimaryKey(Integer authId);

    int updateByPrimaryKeySelective(TblAuth record);

    int updateByPrimaryKey(TblAuth record);

    List<TblAuth> pageSelectAuths();
}