package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblUserErrorLog;
import com.allinfinance.dev.ccs.dal.model.TblUserErrorLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblUserErrorLogMapper {
    long countByExample(TblUserErrorLogExample example);

    int deleteByPrimaryKey(Integer excId);

    int insert(TblUserErrorLog record);

    int insertSelective(TblUserErrorLog record);

    List<TblUserErrorLog> selectByExample(TblUserErrorLogExample example);

    TblUserErrorLog selectByPrimaryKey(Integer excId);

    int updateByExampleSelective(@Param("record") TblUserErrorLog record, @Param("example") TblUserErrorLogExample example);

    int updateByExample(@Param("record") TblUserErrorLog record, @Param("example") TblUserErrorLogExample example);

    int updateByPrimaryKeySelective(TblUserErrorLog record);

    int updateByPrimaryKey(TblUserErrorLog record);
}