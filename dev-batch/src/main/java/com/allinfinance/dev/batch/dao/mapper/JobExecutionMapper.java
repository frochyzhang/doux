package com.allinfinance.dev.batch.dao.mapper;

import com.allinfinance.dev.batch.dao.model.JobExecution;

public interface JobExecutionMapper {
    int deleteByPrimaryKey(Long jobExecutionId);

    int insert(JobExecution record);

    int insertSelective(JobExecution record);

    JobExecution selectByPrimaryKey(Long jobExecutionId);

    int updateByPrimaryKeySelective(JobExecution record);

    int updateByPrimaryKey(JobExecution record);

    int deleteCompletedJobExecution(Long jobExecutionId);
}