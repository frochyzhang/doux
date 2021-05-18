package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.paramvo.LogReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.service.TblOptLogService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author ：Lucas Li
 * @date ：2021/5/17 10:50
 * @description：用户操作日志
 */
@RestController
@RequestMapping("/platform")
public class OptLogController {
    private static final Logger logger = LoggerFactory.getLogger(OptLogController.class);

    @Autowired
    private TblOptLogService tblOptLogService;


    /**
     * 分页带参数查操作日志
     * @param logReqParam
     * @return
     */
    @RequestMapping(path = "/opts", method = RequestMethod.GET)
    public PageInfo<TblUserOptLog> selectOptLogs(@RequestBody LogReqParam logReqParam) {
        logger.info("接受到的参数:currentPage-->{},pageSize-->{}", logReqParam.getCurrent(), logReqParam.getPageSize());
        PageInfo<TblUserOptLog> optLogs = tblOptLogService.pageSelectOptLogs(logReqParam);
        logger.info("查询到的用户列表: {}", optLogs);
        return optLogs;
    }


}
