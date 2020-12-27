package com.allinfinance.dev.batch.dao.service;

import com.allinfinance.dev.batch.dao.mapper.JobExecutionParamsMapper;
import com.allinfinance.dev.batch.dao.model.JobExecutionParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 张勇
 * @description
 * @date 2020/12/24 11:10
 */
@Service
public class JobExecutionParamsServiceImpl implements JobExecutionParamsService {
    @Autowired
    private JobExecutionParamsMapper jobExecutionParamsMapper;

    @Override
    public int insert(JobExecutionParams record) {
        return 0;
    }

    @Override
    public int insertSelective(JobExecutionParams record) {
        return 0;
    }

    @Override
    public List<Map> selectByJobExecutionId(Long jobExecutionId) {
        return jobExecutionParamsMapper.selectByJobExecutionId(jobExecutionId);
    }
}
