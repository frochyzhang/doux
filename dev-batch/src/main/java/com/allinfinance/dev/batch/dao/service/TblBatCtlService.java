package com.allinfinance.dev.batch.dao.service;


import com.allinfinance.dev.batch.dao.model.TblBatCtl;

import java.util.List;
import java.util.Map;

/**
 * TblBatCtlService
 *
 * @author hongmr
 * @date 2017/6/12
 */
public interface TblBatCtlService {
    List<TblBatCtl> selectAll();

    List<Map<String, Object>> selectJobExecution();

    int deleteCompletedJob(Long jobExecutionId);

    Map selectCompletedJobExecution(Long jobExecutionId);
}
