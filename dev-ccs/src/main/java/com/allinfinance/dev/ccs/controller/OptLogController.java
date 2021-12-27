package com.allinfinance.dev.ccs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.paramvo.LogReqParam;
import com.allinfinance.dev.ccs.dal.respdto.UserLogRespDto;
import com.allinfinance.dev.ccs.dal.service.TblOptLogService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


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
    @GetMapping(path = "/opts")
    @ResponseBody
    @OperLog(operModul = "操作日志-日志列表", operType = AosContent.QUERY, operDesc = "分页查询操作日志")
    public Result selectOptLogs(LogReqParam logReqParam, HttpServletRequest request) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//        处理未输入参数时默认的时间
        if (logReqParam.getTime() != null) {
            JSONObject ob = JSON.parseObject(logReqParam.getTime());
            logReqParam.setBeginDate(ob.getString("beginDate"));
            logReqParam.setEndDate(ob.getString("endDate"));
        }
        if (logReqParam.getEndDate() != null) {
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sf.parse(logReqParam.getEndDate()));
                cal.add(Calendar.DATE, 1);
                // 如果接受的直接有endDate直接处理
                logReqParam.setEndDate(sf.format(cal.getTime()));
            } catch (ParseException e) {
                logger.error("日志查询日期格式转换异常!", e);
            }
        }
        logger.info("接受到的分页参数:currentPage-->{},pageSize-->{}", logReqParam.getCurrent(), logReqParam.getPageSize());
        String token = request.getHeader(AosContent.AOS_TOKEN);
        if (!AosContent.SUPERADMIN.equals(JwtUtil.getWeight(token))) {
            logReqParam.setOrg(JwtUtil.getOrg(token));
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
