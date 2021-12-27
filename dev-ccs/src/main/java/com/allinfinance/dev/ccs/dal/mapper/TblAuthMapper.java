package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblAuthExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblAuthMapper {
    long countByExample(TblAuthExample example);

    int deleteByPrimaryKey(String authId);

    int insert(TblAuth record);

    int insertSelective(TblAuth record);

    List<TblAuth> selectByExample(TblAuthExample example);

    TblAuth selectByPrimaryKey(String authId);

    int updateByExampleSelective(@Param("record") TblAuth record, @Param("example") TblAuthExample example);

    int updateByExample(@Param("record") TblAuth record, @Param("example") TblAuthExample example);

    int updateByPrimaryKeySelective(TblAuth record);

    int updateByPrimaryKey(TblAuth record);
}