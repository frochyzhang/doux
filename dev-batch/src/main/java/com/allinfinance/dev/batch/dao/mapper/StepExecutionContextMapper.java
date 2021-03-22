package com.allinfinance.dev.batch.dao.mapper;

import com.allinfinance.dev.batch.dao.model.StepExecutionContext;

public interface StepExecutionContextMapper {
    int deleteByPrimaryKey(Long stepExecutionId);

    int insert(StepExecutionContext record);

    int insertSelective(StepExecutionContext record);

    StepExecutionContext selectByPrimaryKey(Long stepExecutionId);

    int updateByPrimaryKeySelective(StepExecutionContext record);

    int updateByPrimaryKey(StepExecutionContext record);
}