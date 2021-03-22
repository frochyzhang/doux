package com.allinfinance.dev.batch.dao.mapper;

import com.allinfinance.dev.batch.dao.model.StepExecution;

public interface StepExecutionMapper {
    int deleteByPrimaryKey(Long stepExecutionId);

    int insert(StepExecution record);

    int insertSelective(StepExecution record);

    StepExecution selectByPrimaryKey(Long stepExecutionId);

    int updateByPrimaryKeySelective(StepExecution record);

    int updateByPrimaryKey(StepExecution record);
}