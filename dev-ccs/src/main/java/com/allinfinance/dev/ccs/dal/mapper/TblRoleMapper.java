package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;

import java.util.List;

public interface TblRoleMapper {
    int deleteByPrimaryKey(String roleId);

    int insert(TblRole record);

    int insertSelective(TblRole record);

    TblRole selectByPrimaryKey(String roleId);

    int updateByPrimaryKeySelective(TblRole record);

    int updateByPrimaryKey(TblRole record);

    List<TblRole> pageSelectRoles(RoleReqParam roleReqParam);
}