package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;

import java.util.List;

import com.allinfinance.dev.ccs.dal.paramvo.LogReqParam;
import com.allinfinance.dev.ccs.dal.respdto.UserLogRespDto;
import org.apache.ibatis.annotations.Param;

public interface TblUserOptLogMapper {


    int deleteByPrimaryKey(String operId);

    int insert(TblUserOptLog record);

    int insertSelective(TblUserOptLog record);
    

    TblUserOptLog selectByPrimaryKey(String operId);


    int updateByPrimaryKeySelective(TblUserOptLog record);

    int updateByPrimaryKey(TblUserOptLog record);

    List<UserLogRespDto> pageSelectOptLogs(@Param("logReqParam") LogReqParam logReqParam);
}