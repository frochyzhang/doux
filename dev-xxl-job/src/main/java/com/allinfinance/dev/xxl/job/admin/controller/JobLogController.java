package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.common.DateUtils;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.xxl.job.admin.constant.ClearLogTypeEnum;
import com.allinfinance.dev.xxl.job.admin.constant.XxlJobResultCodeEnum;
import com.allinfinance.dev.xxl.job.admin.core.complete.XxlJobCompleter;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobGroup;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobInfo;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobLog;
import com.allinfinance.dev.xxl.job.admin.core.scheduler.XxlJobScheduler;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobGroupDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobInfoDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobLogDao;
import com.allinfinance.dev.xxl.job.admin.dto.LogDetailQueryResponseDTO;
import com.github.pagehelper.PageInfo;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.KillParam;
import com.xxl.job.core.biz.model.LogParam;
import com.xxl.job.core.biz.model.LogResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Api(value = "JobLogController", tags = {"日志管理接口"})
@RestController
@RequestMapping("/job/logs")
public class JobLogController {
    private static final Logger logger = LoggerFactory.getLogger(JobLogController.class);

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    public XxlJobInfoDao xxlJobInfoDao;
    @Resource
    public XxlJobLogDao xxlJobLogDao;

    @GetMapping("/jobGroups")
    @ApiOperation("查询执行器列表")
    public Result index() {
        logger.info("查询执行器列表");
        // 执行器列表
        // TODO: 2021/12/31 权限控制待定，返回所有执行器
        List<XxlJobGroup> xxlJobGroupDaoAll = xxlJobGroupDao.findAll();

        logger.debug("执行器列表: {}", xxlJobGroupDaoAll);
        return Result.success(xxlJobGroupDaoAll);
    }

    @GetMapping
    @ApiOperation("分页查询日志列表")
    public Result pageList(@RequestParam(name = "current") Integer pageNo,
                           @RequestParam(name = "pageSize") Integer pageSize,
                           @RequestParam(name = "jobGroupId", required = false) Integer jobGroupId,
                           @RequestParam(name = "jobId", required = false) Integer jobId,
                           @RequestParam(name = "logStatus", required = false) Integer logStatus,
                           @RequestParam(name = "startDate", required = false) String startDate,
                           @RequestParam(name = "endDate", required = false) String endDate) {
        logger.info("分页查询日志列表, jobGroupId: {}, logStatus: {}, startDate: {}, endDate: {}", jobGroupId, logStatus, startDate, endDate);
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            endTime = calendar.getTime();
            //默认显示一周的日志
            calendar.add(Calendar.DAY_OF_MONTH, -6);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            startTime = calendar.getTime();
        } else {
            startTime = DateUtils.parseDateString(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDateString(endDate));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            endTime = calendar.getTime();
        }

        // page query
        List<XxlJobLog> xxlJobLogList = xxlJobLogDao.pageList((pageNo - 1) * pageSize, pageSize, jobGroupId, jobId, startTime, endTime, logStatus);
        int pageListCount = xxlJobLogDao.pageListCount(jobGroupId, jobId, startTime, endTime, logStatus);
        PageInfo<XxlJobLog> xxlJobLogPageInfo = new PageInfo<>(xxlJobLogList);
        xxlJobLogPageInfo.setTotal(pageListCount);

        logger.info("分页查询日志列表完成");
        logger.debug("日志列表: {}", xxlJobLogList);
        return Result.success(xxlJobLogPageInfo);
    }

    @GetMapping("{jobLogId}")
    @ApiOperation("根据日志id查询日志页面信息")
    public Result logDetail(@PathVariable int jobLogId) {
        logger.info("根据日志id查询日志详细信息, jobLogId: {}", jobLogId);
        // base check
        XxlJobLog jobLog = xxlJobLogDao.load(jobLogId);
        if (jobLog == null) {
            return Result.failure(XxlJobResultCodeEnum.JOB_LOG_NOT_EXIST);
        }

        LogDetailQueryResponseDTO logDetailQueryResponseDTO = new LogDetailQueryResponseDTO();
        logDetailQueryResponseDTO.setTriggerCode(jobLog.getTriggerCode());
        logDetailQueryResponseDTO.setHandleCode(jobLog.getHandleCode());
        logDetailQueryResponseDTO.setExecutorAddress(jobLog.getExecutorAddress());
        logDetailQueryResponseDTO.setTriggerTime(jobLog.getTriggerTime());
        logDetailQueryResponseDTO.setLogId(jobLog.getId());
        logger.info("根据日志id查询日志详细信息完成");
        logger.debug("日志详细信息: {}", logDetailQueryResponseDTO);
        return Result.success(logDetailQueryResponseDTO);
    }

    @GetMapping("/logDetailCat")
    @ApiOperation("生成执行日志页面详细日志信息")
    public Result logDetailCat(@RequestParam(name = "executorAddress") String executorAddress,
                               @RequestParam(name = "triggerTime") long triggerTime,
                               @RequestParam(name = "logId") long logId,
                               @RequestParam(name = "fromLineNum") int fromLineNum) {
        logger.info("生成执行日志页面详细日志信息开始, executorAddress: {}, triggerTime: {}, logId: {}, fromLineNum: {}", executorAddress, triggerTime, logId, fromLineNum);
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(executorAddress);
            ReturnT<LogResult> logResult = executorBiz.log(new LogParam(triggerTime, logId, fromLineNum));

            // is end
            if (logResult.getContent() != null && logResult.getContent().getFromLineNum() > logResult.getContent().getToLineNum()) {
                XxlJobLog jobLog = xxlJobLogDao.load(logId);
                if (jobLog.getHandleCode() > 0) {
                    logResult.getContent().setEnd(true);
                }
            }

            logger.info("生成执行日志页面详细日志信息完成");
            logger.debug("日志信息: {}", logResult);
            return Result.success(logResult);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success(new ReturnT<>(ReturnT.FAIL_CODE, e.getMessage()));
        }
    }

    @PostMapping("/logKill")
    @ApiOperation("终止任务")
    public Result logKill(@RequestParam int jobLogId) {
        logger.info("终止任务, jobLogId: {}", jobLogId);
        // base check
        XxlJobLog jobLog = xxlJobLogDao.load(jobLogId);
        if (jobLog == null) {
            return Result.failure(XxlJobResultCodeEnum.JOB_LOG_NOT_EXIST);
        }
        XxlJobInfo jobInfo = xxlJobInfoDao.loadById(jobLog.getJobId());
        if (jobInfo == null) {
            return Result.failure(XxlJobResultCodeEnum.JOB_INFO_NOT_EXIST);
        }
        if (ReturnT.SUCCESS_CODE != jobLog.getTriggerCode()) {
            return Result.failure(XxlJobResultCodeEnum.JOB_LOG_KILL_DISPATCH_FAILED);
        }

        // request of kill
        ReturnT<String> runResult = null;
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(jobLog.getExecutorAddress());
            runResult = executorBiz.kill(new KillParam(jobInfo.getId()));
        } catch (Exception e) {
            logger.error("终止日志异常", e);
            runResult = new ReturnT<>(ReturnT.FAIL_CODE, e.getMessage());
        }

        if (ReturnT.SUCCESS_CODE == runResult.getCode()) {
            jobLog.setHandleCode(ReturnT.FAIL_CODE);
            jobLog.setHandleMsg("人为操作，主动终止" + ":" + (runResult.getMsg() != null ? runResult.getMsg() : ""));
            jobLog.setHandleTime(new Date());
            XxlJobCompleter.updateHandleInfoAndFinish(jobLog);
            logger.info("终止任务完成");
            return Result.success(runResult.getMsg());
        } else {
            logger.error("终止任务失败");
            return Result.failure(XxlJobResultCodeEnum.JOB_LOG_KILL_FAILED);
        }
    }

    @DeleteMapping("/clearLog")
    @ApiOperation("清理日志")
    public Result clearLog(@RequestParam(name = "jobGroupId") Integer jobGroupId,
                           @RequestParam(name = "jobId") Integer jobId,
                           @RequestParam(name = "type") Integer type) {
        ClearLogTypeEnum clearLogTypeEnum = ClearLogTypeEnum.match(type);
        logger.info("清理日志, jobGroupId: {}, jobId: {}, 清理类型: {}", jobGroupId, jobId, clearLogTypeEnum);
        Date clearBeforeTime = null;
        Integer clearBeforeNum = null;
        switch (clearLogTypeEnum) {
            case CLEAR_LOG_ONE_MONTH_AGO:
                // 清理一个月之前日志数据
                clearBeforeTime = DateUtil.addMonths(new Date(), -1);
                break;
            case CLEAR_LOG_THREE_MONTHS_AGO:
                // 清理三个月之前日志数据
                clearBeforeTime = DateUtil.addMonths(new Date(), -3);
                break;
            case CLEAR_LOG_SIX_MONTHS_AGO:
                // 清理六个月之前日志数据
                clearBeforeTime = DateUtil.addMonths(new Date(), -6);
                break;
            case CLEAR_LOG_ONE_YEAR_AGO:
                // 清理一年之前日志数据
                clearBeforeTime = DateUtil.addYears(new Date(), -1);
                break;
            case CLEAR_LOG_ONE_THOUSAND_LEFT:
                // 清理一千条以前日志数据
                clearBeforeNum = 1000;
                break;
            case CLEAR_LOG_TEN_THOUSAND_LEFT:
                // 清理一万条以前日志数据
                clearBeforeNum = 10000;
                break;
            case CLEAR_LOG_THIRTY_THOUSAND_LEFT:
                // 清理三万条以前日志数据
                clearBeforeNum = 30000;
                break;
            case CLEAR_LOG_ONE_HUNDRED_THOUSAND_LEFT:
                // 清理十万条以前日志数据
                clearBeforeNum = 100000;
                break;
            case CLEAR_LOG_ALL:
                // 清理所有日志数据
                clearBeforeNum = 0;
                break;
            default:
                return Result.failure(XxlJobResultCodeEnum.INVALID_CLEAR_LOG_TYPE);
        }

        List<Long> logIds = null;
        do {
            logIds = xxlJobLogDao.findClearLogIds(jobGroupId, jobId, clearBeforeTime, clearBeforeNum, 1000);
            if (logIds != null && logIds.size() > 0) {
                xxlJobLogDao.clearLog(logIds);
            }
        } while (logIds != null && logIds.size() > 0);

        logger.info("清理日志完成");
        return Result.success();
    }
}
