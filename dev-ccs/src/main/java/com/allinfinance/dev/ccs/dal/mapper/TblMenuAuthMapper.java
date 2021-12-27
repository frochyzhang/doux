package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuthExample;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuthKey;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblMenuAuthMapper {
    long countByExample(TblMenuAuthExample example);

    int deleteByPrimaryKey(TblMenuAuthKey key);

    int insert(TblMenuAuth record);

    int insertSelective(TblMenuAuth record);

    List<TblMenuAuth> selectByExample(TblMenuAuthExample example);

    TblMenuAuth selectByPrimaryKey(TblMenuAuthKey key);

    int updateByExampleSelective(@Param("record") TblMenuAuth record, @Param("example") TblMenuAuthExample example);

    int updateByExample(@Param("record") TblMenuAuth record, @Param("example") TblMenuAuthExample example);

    int updateByPrimaryKeySelective(TblMenuAuth record);

    int updateByPrimaryKey(TblMenuAuth record);

    List<TblMenuAuth> selectBatchIds(ArrayList<String> authIds);
}