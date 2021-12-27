package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.xxl.job.admin.core.complete.XxlJobCompleter;
import com.allinfinance.dev.xxl.job.admin.core.exception.XxlJobException;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobGroup;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobInfo;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobLog;
import com.allinfinance.dev.xxl.job.admin.core.scheduler.XxlJobScheduler;
import com.allinfinance.dev.xxl.job.admin.core.util.I18nUtil;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobGroupDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobInfoDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobLogDao;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.KillParam;
import com.xxl.job.core.biz.model.LogParam;
import com.xxl.job.core.biz.model.LogResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@RestController
@RequestMapping("/logs")
public class JobLogController {
    private static final Logger logger = LoggerFactory.getLogger(JobLogController.class);

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    public XxlJobInfoDao xxlJobInfoDao;
    @Resource
    public XxlJobLogDao xxlJobLogDao;

    @GetMapping("constants")
    public Result index(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") Integer jobId) {

        // 执行器列表
        List<XxlJobGroup> xxlJobGroupDaoAll = xxlJobGroupDao.findAll();

        // filter group
        List<XxlJobGroup> jobGroupList = JobInfoController.filterJobGroupByRole(request, xxlJobGroupDaoAll);
        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new XxlJobException(I18nUtil.getString("jobgroup_empty"));
        }

        Map<String, Object> maps = new HashMap<>();
        maps.put("JobGroupList", jobGroupList);

        // 任务
        if (jobId > 0) {
            XxlJobInfo jobInfo = xxlJobInfoDao.loadById(jobId);
            if (jobInfo == null) {
                throw new RuntimeException(I18nUtil.getString("jobinfo_field_id") + I18nUtil.getString("system_unvalid"));
            }

            maps.put("jobInfo", jobInfo);

            // valid permission
            JobInfoController.validPermission(request, jobInfo.getJobGroup());
        }

        return Result.success(maps);
    }

    @GetMapping("{jobGroup}")
    public Result getJobsByGroup(@PathVariable int jobGroup) {
        List<XxlJobInfo> list = xxlJobInfoDao.getJobsByGroup(jobGroup);
        return Result.success(list);
    }

    @GetMapping
    public Result pageList(HttpServletRequest request,
                           @RequestParam(required = false, defaultValue = "0") int start,
                           @RequestParam(required = false, defaultValue = "10") int length,
                           int jobGroup, int jobId, int logStatus, String filterTime) {

        // valid permission
        JobInfoController.validPermission(request, jobGroup);    // 仅管理员支持查询全部；普通用户仅支持查询有权限的 jobGroup

        // parse param
        Date triggerTimeStart = null;
        Date triggerTimeEnd = null;
        if (filterTime != null && filterTime.trim().length() > 0) {
            String[] temp = filterTime.split(" - ");
            if (temp.length == 2) {
                triggerTimeStart = DateUtil.parseDateTime(temp[0]);
                triggerTimeEnd = DateUtil.parseDateTime(temp[1]);
            }
        }

        // page query
        List<XxlJobLog> list = xxlJobLogDao.pageList(start, length, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);
        int listCount = xxlJobLogDao.pageListCount(start, length, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);

        // package result
        Map<String, Object> maps = new HashMap<>();
        maps.put("recordsTotal", listCount);        // 总记录数
        maps.put("recordsFiltered", listCount);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return Result.success(maps);
    }

    @GetMapping("{jobLogId}")
    public Result logDetailPage(@PathVariable int jobLogId) {

        // base check
        XxlJobLog jobLog = xxlJobLogDao.load(jobLogId);
        if (jobLog == null) {
            throw new RuntimeException(I18nUtil.getString("joblog_logid_unvalid"));
        }

        Map<String, Object> maps = new HashMap<>();

        maps.put("triggerCode", jobLog.getTriggerCode());
        maps.put("handleCode", jobLog.getHandleCode());
        maps.put("executorAddress", jobLog.getExecutorAddress());
        maps.put("triggerTime", jobLog.getTriggerTime().getTime());
        maps.put("logId", jobLog.getId());
        return Result.success(maps);
    }

    @RequestMapping("/logDetailCat")
    @ResponseBody
    public Result logDetailCat(String executorAddress, long triggerTime, long logId, int fromLineNum) {
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

            return Result.success(logResult);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success(new ReturnT<>(ReturnT.FAIL_CODE, e.getMessage()));
        }
    }

    @RequestMapping("/logKill")
    @ResponseBody
    public Result logKill(int id) {
        // base check
        XxlJobLog log = xxlJobLogDao.load(id);
        XxlJobInfo jobInfo = xxlJobInfoDao.loadById(log.getJobId());
        if (jobInfo == null) {
            return Result.failure("500", I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }
        if (ReturnT.SUCCESS_CODE != log.getTriggerCode()) {
            return Result.failure("500", I18nUtil.getString("joblog_kill_log_limit"));
        }

        // request of kill
        ReturnT<String> runResult = null;
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(log.getExecutorAddress());
            runResult = executorBiz.kill(new KillParam(jobInfo.getId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            runResult = new ReturnT<String>(500, e.getMessage());
        }

        if (ReturnT.SUCCESS_CODE == runResult.getCode()) {
            log.setHandleCode(ReturnT.FAIL_CODE);
            log.setHandleMsg(I18nUtil.getString("joblog_kill_log_byman") + ":" + (runResult.getMsg() != null ? runResult.getMsg() : ""));
            log.setHandleTime(new Date());
            XxlJobCompleter.updateHandleInfoAndFinish(log);
            return Result.success(runResult.getMsg());
        } else {
            return Result.failure("500", runResult.getMsg());
        }
    }

    @RequestMapping("/clearLog")
    @ResponseBody
    public Result clearLog(int jobGroup, int jobId, int type) {

        Date clearBeforeTime = null;
        int clearBeforeNum = 0;
        if (type == 1) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -1);    // 清理一个月之前日志数据
        } else if (type == 2) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -3);    // 清理三个月之前日志数据
        } else if (type == 3) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -6);    // 清理六个月之前日志数据
        } else if (type == 4) {
            clearBeforeTime = DateUtil.addYears(new Date(), -1);    // 清理一年之前日志数据
        } else if (type == 5) {
            clearBeforeNum = 1000;        // 清理一千条以前日志数据
        } else if (type == 6) {
            clearBeforeNum = 10000;        // 清理一万条以前日志数据
        } else if (type == 7) {
            clearBeforeNum = 30000;        // 清理三万条以前日志数据
        } else if (type == 8) {
            clearBeforeNum = 100000;    // 清理十万条以前日志数据
        } else if (type == 9) {
            clearBeforeNum = 0;            // 清理所有日志数据
        } else {
            return Result.failure("" + ReturnT.FAIL_CODE, I18nUtil.getString("joblog_clean_type_unvalid"));
        }

        List<Long> logIds = null;
        do {
            logIds = xxlJobLogDao.findClearLogIds(jobGroup, jobId, clearBeforeTime, clearBeforeNum, 1000);
            if (logIds != null && logIds.size() > 0) {
                xxlJobLogDao.clearLog(logIds);
            }
        } while (logIds != null && logIds.size() > 0);

        return Result.success();
    }

}
