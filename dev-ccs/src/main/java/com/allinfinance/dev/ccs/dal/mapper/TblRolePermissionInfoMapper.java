package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;

import java.util.List;

public interface TblRolePermissionInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TblRolePermissionInfo record);

    int insertSelective(TblRolePermissionInfo record);

    TblRolePermissionInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TblRolePermissionInfo record);

    int updateByPrimaryKey(TblRolePermissionInfo record);

    List<TblRolePermissionInfo> getRolePermissionInfByRoleId(String roleId);

    List<TblRolePermissionInfo> getRoleIdByPermissionCode(String permissionCode);

    int deleteRolePermissionInfoByRoleId(String roleId);
}