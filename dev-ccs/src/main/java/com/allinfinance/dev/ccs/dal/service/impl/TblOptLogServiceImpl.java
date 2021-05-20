package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.dal.mapper.TblUserOptLogMapper;
import com.allinfinance.dev.ccs.dal.paramvo.LogReqParam;
import com.allinfinance.dev.ccs.dal.respdto.UserLogRespDto;
import com.allinfinance.dev.ccs.dal.service.TblOptLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    public PageInfo<UserLogRespDto> pageSelectOptLogs(LogReqParam logReqParam) {
        PageHelper.startPage(logReqParam.getCurrent(), logReqParam.getPageSize());
        List<UserLogRespDto> optLogs = tblUserOptLogMapper.pageSelectOptLogs(logReqParam);
        //将list转为对象数组
        UserLogRespDto[] logs= new UserLogRespDto[optLogs.size()];
        logs = optLogs.toArray(logs);
        return new PageInfo<UserLogRespDto>(optLogs);
    }
}
