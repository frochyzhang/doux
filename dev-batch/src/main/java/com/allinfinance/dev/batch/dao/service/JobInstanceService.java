package com.allinfinance.dev.batch.dao.service;

import com.allinfinance.dev.batch.dao.model.JobInstance;

/**
 * @author 张勇
 * @description
 * @date 2020/12/22 17:32
 */
public interface JobInstanceService {

    int deleteByPrimaryKey(Long jobInstanceId);

    int insert(JobInstance record);

    int insertSelective(JobInstance record);

    JobInstance selectByPrimaryKey(Long jobInstanceId);

    int updateByPrimaryKeySelective(JobInstance record);

    int updateByPrimaryKey(JobInstance record);
}
