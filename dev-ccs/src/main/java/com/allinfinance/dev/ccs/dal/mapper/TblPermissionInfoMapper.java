package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblPermissionInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblPermissionInfoMapper {
    int deleteByPrimaryKey(String permissioncode);

    int insert(TblPermissionInfo record);

    int insertSelective(TblPermissionInfo record);

    TblPermissionInfo selectByPrimaryKey(String permissioncode);

    int updateByPrimaryKeySelective(TblPermissionInfo record);

    int updateByPrimaryKey(TblPermissionInfo record);

    TblPermissionInfo selectByRequestUrl(@Param("url") String requestUrl);

    List<TblPermissionInfo> selectPermissionInfos();
}