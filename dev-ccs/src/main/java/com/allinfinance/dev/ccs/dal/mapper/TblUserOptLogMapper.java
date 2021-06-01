package com.allinfinance.dev.ccs.dal.mapper;

import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.paramvo.LogReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.respdto.UserLogRespDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblUserOptLogMapper {
    int deleteByPrimaryKey(Integer operationId);

    int insert(TblUserOptLog record);

    int insertSelective(TblUserOptLog record);

    TblUserOptLog selectByPrimaryKey(Integer operationId);

    int updateByPrimaryKeySelective(TblUserOptLog record);

    int updateByPrimaryKey(TblUserOptLog record);

    List<UserLogRespDto> pageSelectOptLogs(@Param("logReqParam") LogReqParam logReqParam);
}