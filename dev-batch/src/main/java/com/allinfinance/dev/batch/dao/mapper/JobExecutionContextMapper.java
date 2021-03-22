package com.allinfinance.dev.batch.dao.mapper;

import com.allinfinance.dev.batch.dao.model.JobExecutionContext;

public interface JobExecutionContextMapper {
    int deleteByPrimaryKey(Long jobExecutionId);

    int insert(JobExecutionContext record);

    int insertSelective(JobExecutionContext record);

    JobExecutionContext selectByPrimaryKey(Long jobExecutionId);

    int updateByPrimaryKeySelective(JobExecutionContext record);

    int updateByPrimaryKey(JobExecutionContext record);
}