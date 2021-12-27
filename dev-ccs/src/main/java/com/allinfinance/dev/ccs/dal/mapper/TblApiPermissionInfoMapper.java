package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblApiPermissionInfo;
import com.allinfinance.dev.ccs.dal.model.TblApiPermissionInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblApiPermissionInfoMapper {
    long countByExample(TblApiPermissionInfoExample example);

    int deleteByPrimaryKey(String permissioncode);

    int insert(TblApiPermissionInfo record);

    int insertSelective(TblApiPermissionInfo record);

    List<TblApiPermissionInfo> selectByExample(TblApiPermissionInfoExample example);

    TblApiPermissionInfo selectByPrimaryKey(String permissioncode);

    int updateByExampleSelective(@Param("record") TblApiPermissionInfo record, @Param("example") TblApiPermissionInfoExample example);

    int updateByExample(@Param("record") TblApiPermissionInfo record, @Param("example") TblApiPermissionInfoExample example);

    int updateByPrimaryKeySelective(TblApiPermissionInfo record);

    int updateByPrimaryKey(TblApiPermissionInfo record);
}