package com.allinfinance.dev.batch.dao.service;

import com.allinfinance.dev.batch.dao.mapper.JobExecutionMapper;
import com.allinfinance.dev.batch.dao.model.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 张勇
 * @description
 * @date 2020/12/22 17:38
 */
@Service
public class JobExecutionServiceImpl implements JobExecutionService {

    @Autowired
    private JobExecutionMapper jobExecutionMapper;

    @Override
    public int deleteByPrimaryKey(Long jobExecutionId) {
        return 0;
    }

    @Override
    public int insert(JobExecution record) {
        return 0;
    }

    @Override
    public int insertSelective(JobExecution record) {
        return 0;
    }

    @Override
    public JobExecution selectByPrimaryKey(Long jobExecutionId) {
        return jobExecutionMapper.selectByPrimaryKey(jobExecutionId);
    }

    @Override
    public int updateByPrimaryKeySelective(JobExecution record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(JobExecution record) {
        return 0;
    }
}
