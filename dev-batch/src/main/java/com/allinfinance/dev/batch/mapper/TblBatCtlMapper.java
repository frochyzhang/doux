package com.allinfinance.dev.batch.mapper;


import com.allinfinance.dev.batch.model.TblBatCtl;

import java.util.List;
import java.util.Map;

public interface TblBatCtlMapper {
    int insert(TblBatCtl record);

    int insertSelective(TblBatCtl record);

    List<TblBatCtl> selectAll();

    List<Map<String, Object>> selectJobExecution();

    int deleteCompletedJobExecution(Long jobExecutionId);

    int deleteCompletedJobContext(Long jobExecutionId);

    int deleteCompletedJobParams(Long jobExecutionId);

    int deleteCompletedStepExecution(Long jobExecutionId);

    int deleteCompletedStepExecutionContext(Long jobExecutionId);

    Map selectCompletedJobExecution(Long jobExecutionId);
}