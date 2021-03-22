package com.allinfinance.dev.batch.dao.mapper;

import com.allinfinance.dev.batch.dao.model.JobInstance;

public interface JobInstanceMapper {
    int deleteByPrimaryKey(Long jobInstanceId);

    int insert(JobInstance record);

    int insertSelective(JobInstance record);

    JobInstance selectByPrimaryKey(Long jobInstanceId);

    int updateByPrimaryKeySelective(JobInstance record);

    int updateByPrimaryKey(JobInstance record);
}