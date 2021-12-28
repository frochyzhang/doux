package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.dal.mapper.TblUserOptLogMapper;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLogExample;
import com.allinfinance.dev.ccs.dal.paramvo.LogReqParam;
import com.allinfinance.dev.ccs.dal.service.TblOptLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：Lucas Li
 * @project :IntelliJ IDEA
 * @date ：2021/5/13 18:54
 * @description：用户持久层服务
 */
@Service
public class TblOptLogServiceImpl implements TblOptLogService {

    @Autowired
    private TblUserOptLogMapper tblUserOptLogMapper;

    @Override
    public PageInfo<TblUserOptLog> pageSelectOptLogs(LogReqParam logReqParam) {
        PageHelper.startPage(logReqParam.getCurrent(), logReqParam.getPageSize());
        TblUserOptLogExample example = new TblUserOptLogExample();
        TblUserOptLogExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(logReqParam.getOperUserName())) {
            criteria.andOperUserNameLike("%" + logReqParam.getOperUserName() + "%");
        }
        if (StringUtils.isNotBlank(logReqParam.getOrg())) {
            criteria.andOrgEqualTo(logReqParam.getOrg());
        }
        if (StringUtils.isNotBlank(logReqParam.getOperType())) {
            criteria.andOperTypeEqualTo(logReqParam.getOperType());
        }
        if (StringUtils.isNotBlank(logReqParam.getOperDesc())) {
            criteria.andOperDescLike("%" + logReqParam.getOperDesc() + "%");
        }
        if (StringUtils.isNotBlank(logReqParam.getBeginDate().toString()) && StringUtils.isNotBlank(logReqParam.getEndDate().toString())) {
            criteria.andOperCreateTimeBetween(logReqParam.getBeginDate(), logReqParam.getEndDate());
        }
        List<TblUserOptLog> optLogs = tblUserOptLogMapper.selectByExample(example);
        return new PageInfo<>(optLogs);
    }

    @Override
    public int insertLog(TblUserOptLog tblUserOptLog) {
        return tblUserOptLogMapper.insert(tblUserOptLog);
    }
}
