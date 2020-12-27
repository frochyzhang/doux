package com.allinfinance.dev.batch.dao.service;

import com.allinfinance.dev.batch.dao.model.JobExecutionParams;

import java.util.List;
import java.util.Map;

/**
 * @author 张勇
 * @description
 * @date 2020/12/22 17:32
 */
public interface JobExecutionParamsService {
    int insert(JobExecutionParams record);

    int insertSelective(JobExecutionParams record);

    List<Map> selectByJobExecutionId(Long jobExecutionId);
}
