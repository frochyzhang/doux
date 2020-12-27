package com.allinfinance.dev.batch.dao.service;

import com.allinfinance.dev.batch.dao.mapper.JobExecutionMapper;
import com.allinfinance.dev.batch.dao.mapper.TblBatCtlMapper;
import com.allinfinance.dev.batch.dao.model.TblBatCtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * TblBatCtlServiceImpl
 *
 * @author hongmr
 * @date 2017/6/12
 */
@Service("tblBatCtlService")
public class TblBatCtlServiceImpl implements TblBatCtlService {
    @Autowired
    private TblBatCtlMapper tblBatCtlMapper;
    @Autowired
    private JobExecutionMapper jobExecutionMapper;

    @Override
    public List<TblBatCtl> selectAll() {
        return tblBatCtlMapper.selectAll();
    }

    @Override
    public List<Map<String, Object>> selectJobExecution() {
        return tblBatCtlMapper.selectJobExecution();
    }

    @Override
    public int deleteCompletedJob(Long jobExecutionId) {
//        tblBatCtlMapper.deleteCompletedStepExecutionContext(jobExecutionId);
//        tblBatCtlMapper.deleteCompletedStepExecution(jobExecutionId);
//        tblBatCtlMapper.deleteCompletedJobParams(jobExecutionId);
//        tblBatCtlMapper.deleteCompletedJobContext(jobExecutionId);
        return jobExecutionMapper.deleteCompletedJobExecution(jobExecutionId);
    }

    @Override
    public Map selectCompletedJobExecution(Long jobExecutionId) {
        return tblBatCtlMapper.selectCompletedJobExecution(jobExecutionId);
    }
}
