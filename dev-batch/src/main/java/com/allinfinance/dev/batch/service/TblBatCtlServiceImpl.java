package com.allinfinance.dev.batch.service;

import com.allinfinance.dev.batch.mapper.TblBatCtlMapper;
import com.allinfinance.dev.batch.model.TblBatCtl;
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
        return tblBatCtlMapper.deleteCompletedJobExecution(jobExecutionId);
    }

    @Override
    public Map selectCompletedJobExecution(Long jobExecutionId) {
        return tblBatCtlMapper.selectCompletedJobExecution(jobExecutionId);
    }
}
