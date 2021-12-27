package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblUserOptLogMapper {
    long countByExample(TblUserOptLogExample example);

    int deleteByPrimaryKey(Integer operId);

    int insert(TblUserOptLog record);

    int insertSelective(TblUserOptLog record);

    List<TblUserOptLog> selectByExample(TblUserOptLogExample example);

    TblUserOptLog selectByPrimaryKey(Integer operId);

    int updateByExampleSelective(@Param("record") TblUserOptLog record, @Param("example") TblUserOptLogExample example);

    int updateByExample(@Param("record") TblUserOptLog record, @Param("example") TblUserOptLogExample example);

    int updateByPrimaryKeySelective(TblUserOptLog record);

    int updateByPrimaryKey(TblUserOptLog record);
}