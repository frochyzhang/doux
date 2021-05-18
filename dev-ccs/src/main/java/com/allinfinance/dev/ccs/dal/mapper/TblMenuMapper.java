package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblMenu;

public interface TblMenuMapper {
    int deleteByPrimaryKey(Integer menuId);

    int insert(TblMenu record);

    int insertSelective(TblMenu record);

    TblMenu selectByPrimaryKey(Integer menuId);

    int updateByPrimaryKeySelective(TblMenu record);

    int updateByPrimaryKey(TblMenu record);
}