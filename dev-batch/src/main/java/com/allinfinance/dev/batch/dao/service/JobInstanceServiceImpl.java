package com.allinfinance.dev.batch.dao.service;

import com.allinfinance.dev.batch.dao.mapper.JobInstanceMapper;
import com.allinfinance.dev.batch.dao.model.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 张勇
 * @description
 * @date 2020/12/24 11:05
 */
@Service
public class JobInstanceServiceImpl implements JobInstanceService {

    @Autowired
    private JobInstanceMapper jobInstanceMapper;

    @Override
    public int deleteByPrimaryKey(Long jobInstanceId) {
        return 0;
    }

    @Override
    public int insert(JobInstance record) {
        return 0;
    }

    @Override
    public int insertSelective(JobInstance record) {
        return 0;
    }

    @Override
    public JobInstance selectByPrimaryKey(Long jobInstanceId) {
        return jobInstanceMapper.selectByPrimaryKey(jobInstanceId);
    }

    @Override
    public int updateByPrimaryKeySelective(JobInstance record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(JobInstance record) {
        return 0;
    }
}
