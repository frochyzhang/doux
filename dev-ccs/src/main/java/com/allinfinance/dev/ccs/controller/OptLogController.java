package com.allinfinance.dev.ccs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.paramvo.LogReqParam;
import com.allinfinance.dev.ccs.dal.respdto.UserLogRespDto;
import com.allinfinance.dev.ccs.dal.service.TblOptLogService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


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
     *
     * @param logReqParam
     * @return
     */
    @RequestMapping(path = "/opts", method = RequestMethod.GET)
    @ResponseBody
    @OperLog(operModul = "操作日志-日志列表",operType = AosContent.QUERY,operDesc = "分页查询操作日志")
    public Result selectOptLogs(LogReqParam logReqParam, HttpServletRequest request) {
        if (logReqParam.getTime()!=null){
            JSONObject ob= JSON.parseObject(logReqParam.getTime());
            logReqParam.setBeginDate(ob.getString("beginDate"));
            logReqParam.setEndDate(ob.getString("endDate"));
        }
        logger.info("接受到的分页参数:currentPage-->{},pageSize-->{}", logReqParam.getCurrent(), logReqParam.getPageSize());
        String token = request.getHeader( AosContent.AOS_TOKEN);
        String org = JwtUtil.getOrg(token);
        logger.info("获取当前操作用户的机构号:org-->{}", org);
        if (org != null && org.length() != 0) {
            //当当前的用户是超级管理员时显示所有列表
            if (org.equals(AosContent.ALLINFINANCE_ORG)) {
                logReqParam.setOrg(null);
            } else {
                logReqParam.setOrg(org);
            }
        }
        PageInfo<UserLogRespDto> optLogs;
        try {
            optLogs = tblOptLogService.pageSelectOptLogs(logReqParam);
        } catch (Exception e) {
            logger.error("查询用户操作日志列表异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("查询到的用户列表: {}", optLogs);
        return Result.success(optLogs);
    }
}
