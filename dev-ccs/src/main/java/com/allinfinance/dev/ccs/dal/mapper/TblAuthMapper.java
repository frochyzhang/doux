package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblAuthMapper {
    int deleteByPrimaryKey(String authId);

    int insert(TblAuth record);

    int insertSelective(TblAuth record);

    TblAuth selectByPrimaryKey(String authId);

    int updateByPrimaryKeySelective(TblAuth record);

    int updateByPrimaryKey(TblAuth record);

    List<TblAuth> pageSelectAuths(@Param("authReqParam") AuthReqParam authReqParam);

    List<TblAuth> selectAuths();

    int invalidateAuth(@Param("authId") String authId,@Param("isAvailable") String isAvailable);
}