package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthExample;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TblRoleAuthMapper {
    long countByExample(TblRoleAuthExample example);

    int deleteByExample(TblRoleAuthExample example);

    int deleteByPrimaryKey(TblRoleAuthKey key);

    int insert(TblRoleAuth record);

    int insertSelective(TblRoleAuth record);

    List<TblRoleAuth> selectByExample(TblRoleAuthExample example);

    TblRoleAuth selectByPrimaryKey(TblRoleAuthKey key);

    int updateByExampleSelective(@Param("record") TblRoleAuth record, @Param("example") TblRoleAuthExample example);

    int updateByExample(@Param("record") TblRoleAuth record, @Param("example") TblRoleAuthExample example);

    int updateByPrimaryKeySelective(TblRoleAuth record);

    int updateByPrimaryKey(TblRoleAuth record);

}