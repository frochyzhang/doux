package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblUserErrorLog;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblUserErrorLogMapper {

    int deleteByPrimaryKey(String excId);

    int insert(TblUserErrorLog record);

    int insertSelective(TblUserErrorLog record);


    TblUserErrorLog selectByPrimaryKey(String excId);

    int updateByPrimaryKeySelective(TblUserErrorLog record);

    int updateByPrimaryKey(TblUserErrorLog record);
}