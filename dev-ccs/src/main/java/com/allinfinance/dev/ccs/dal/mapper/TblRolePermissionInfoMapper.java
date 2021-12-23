package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblRolePermissionInfoMapper {
    long countByExample(TblRolePermissionInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblRolePermissionInfo record);

    int insertSelective(TblRolePermissionInfo record);

    List<TblRolePermissionInfo> selectByExample(TblRolePermissionInfoExample example);

    TblRolePermissionInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblRolePermissionInfo record, @Param("example") TblRolePermissionInfoExample example);

    int updateByExample(@Param("record") TblRolePermissionInfo record, @Param("example") TblRolePermissionInfoExample example);

    int updateByPrimaryKeySelective(TblRolePermissionInfo record);

    int updateByPrimaryKey(TblRolePermissionInfo record);
}