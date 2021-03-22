package com.allinfinance.dev.batch.dao.mapper;


import com.allinfinance.dev.batch.dao.model.TblBatCtl;

import java.util.List;
import java.util.Map;

public interface TblBatCtlMapper {
    int insert(TblBatCtl record);

    int insertSelective(TblBatCtl record);

    List<TblBatCtl> selectAll();

    List<Map<String, Object>> selectJobExecution();

    Map selectCompletedJobExecution(Long jobExecutionId);
}