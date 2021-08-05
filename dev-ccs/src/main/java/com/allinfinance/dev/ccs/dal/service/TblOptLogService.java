package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.paramvo.LogReqParam;
import com.allinfinance.dev.ccs.dal.respdto.UserLogRespDto;
import com.github.pagehelper.PageInfo;

/**
 * @author ：Lucas Li
 * @project :IntelliJ IDEA
 * @date ：2021/5/13 18:54
 * @description：用户持久层服务
 */

public interface TblOptLogService {
    public PageInfo<UserLogRespDto> pageSelectOptLogs(LogReqParam logReqParam) ;
    int insertLog(TblUserOptLog tblUserOptLog);
}
