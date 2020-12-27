package com.allinfinance.dev.batch.dao.service;

import com.allinfinance.dev.batch.dao.model.JobExecution;

/**
 * @author 张勇
 * @description
 * @date 2020/12/22 17:32
 */
public interface JobExecutionService {
    int deleteByPrimaryKey(Long jobExecutionId);

    int insert(JobExecution record);

    int insertSelective(JobExecution record);

    JobExecution selectByPrimaryKey(Long jobExecutionId);

    int updateByPrimaryKeySelective(JobExecution record);

    int updateByPrimaryKey(JobExecution record);
}
