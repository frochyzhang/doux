package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.mapper.TblUserMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblUserOptLogMapper;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.paramvo.LogReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ：Lucas Li
 * @project :IntelliJ IDEA
 * @date ：2021/5/13 18:54
 * @description：用户持久层服务
 */
@Service
public class TblOptLogService {

    @Autowired
    private TblUserOptLogMapper tblUserOptLogMapper;

    public PageInfo<TblUserOptLog> pageSelectOptLogs(LogReqParam logReqParam) {
        PageHelper.startPage(logReqParam.getCurrent(), logReqParam.getPageSize());
        List<TblUserOptLog> optLogs = tblUserOptLogMapper.pageSelectOptLogs(logReqParam);
        return new PageInfo<TblUserOptLog>(optLogs);
    }
}
