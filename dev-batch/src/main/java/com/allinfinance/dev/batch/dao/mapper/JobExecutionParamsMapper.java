package com.allinfinance.dev.batch.dao.mapper;

import com.allinfinance.dev.batch.dao.model.JobExecutionParams;

import java.util.List;
import java.util.Map;

public interface JobExecutionParamsMapper {
    int insert(JobExecutionParams record);

    int insertSelective(JobExecutionParams record);

    List<Map> selectByJobExecutionId(Long jobExecutionId);
}