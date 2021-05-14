package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblRole;

import java.util.List;

public interface TblRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(TblRole record);

    int insertSelective(TblRole record);

    TblRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(TblRole record);

    int updateByPrimaryKey(TblRole record);

    List<TblRole> pageSelectRoles();
}