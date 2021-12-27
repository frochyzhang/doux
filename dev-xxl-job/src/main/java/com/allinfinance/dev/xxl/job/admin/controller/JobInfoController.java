package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.xxl.job.admin.core.exception.XxlJobException;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobGroup;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobInfo;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobUser;
import com.allinfinance.dev.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.allinfinance.dev.xxl.job.admin.core.scheduler.MisfireStrategyEnum;
import com.allinfinance.dev.xxl.job.admin.core.scheduler.ScheduleTypeEnum;
import com.allinfinance.dev.xxl.job.admin.core.thread.JobScheduleHelper;
import com.allinfinance.dev.xxl.job.admin.core.thread.JobTriggerPoolHelper;
import com.allinfinance.dev.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.allinfinance.dev.xxl.job.admin.core.util.I18nUtil;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobGroupDao;
import com.allinfinance.dev.xxl.job.admin.service.LoginService;
import com.allinfinance.dev.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
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
@RequestMapping("/jobs")
public class JobInfoController {
    private static final Logger logger = LoggerFactory.getLogger(JobInfoController.class);

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobService xxlJobService;

    @GetMapping("constants")
    public Result index(HttpServletRequest request, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

        Map<String, Object> map = new HashMap<>();
        // 枚举-字典
        map.put("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());        // 路由策略-列表
        map.put("GlueTypeEnum", GlueTypeEnum.values());                                // Glue类型-字典
        map.put("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());        // 阻塞处理策略-字典
        map.put("ScheduleTypeEnum", ScheduleTypeEnum.values());                        // 调度类型
        map.put("MisfireStrategyEnum", MisfireStrategyEnum.values());                    // 调度过期策略

        // 执行器列表
        List<XxlJobGroup> xxlJobGroupDaoAll = xxlJobGroupDao.findAll();

        // filter group
        List<XxlJobGroup> jobGroupList = filterJobGroupByRole(request, xxlJobGroupDaoAll);
        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new XxlJobException(I18nUtil.getString("jobgroup_empty"));
        }

        map.put("JobGroupList", jobGroupList);
        map.put("jobGroup", jobGroup);


        return Result.success(map);
    }

    public static List<XxlJobGroup> filterJobGroupByRole(HttpServletRequest request, List<XxlJobGroup> jobGroupListAll) {
        List<XxlJobGroup> jobGroupList = new ArrayList<>();
        if (jobGroupListAll != null && jobGroupListAll.size() > 0) {
            XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
            if (loginUser.getRole() == 1) {
                jobGroupList = jobGroupListAll;
            } else {
                List<String> groupIdStrs = new ArrayList<>();
                if (loginUser.getPermission() != null && loginUser.getPermission().trim().length() > 0) {
                    groupIdStrs = Arrays.asList(loginUser.getPermission().trim().split(","));
                }
                for (XxlJobGroup groupItem : jobGroupListAll) {
                    if (groupIdStrs.contains(String.valueOf(groupItem.getId()))) {
                        jobGroupList.add(groupItem);
                    }
                }
            }
        }
        return jobGroupList;
    }

    public static void validPermission(HttpServletRequest request, int jobGroup) {
        XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
        if (!loginUser.validPermission(jobGroup)) {
            throw new RuntimeException(I18nUtil.getString("system_permission_limit") + "[username=" + loginUser.getUsername() + "]");
        }
    }

    @GetMapping
    public Result pageList(@RequestParam(required = false, defaultValue = "0") int start,
                           @RequestParam(required = false, defaultValue = "10") int length,
                           int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {

        return Result.success(xxlJobService.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author));
    }

    @PostMapping
    public Result add(XxlJobInfo jobInfo) {
        return Result.success(xxlJobService.add(jobInfo));
    }

    @PutMapping
    public Result update(XxlJobInfo jobInfo) {
        return Result.success(xxlJobService.update(jobInfo));
    }

    @DeleteMapping("{jobId}")
    public Result remove(@PathVariable int jobId) {
        return Result.success(xxlJobService.remove(jobId));
    }

    @PutMapping("{jobId}/stop")
    public Result pause(@PathVariable int jobId) {
        return Result.success(xxlJobService.stop(jobId));
    }

    @PutMapping("{jobId}/start")
    public Result start(@PathVariable int jobId) {
        return Result.success(xxlJobService.start(jobId));
    }

    @PutMapping("{jobId}/trigger")
    public Result triggerJob(@PathVariable int jobId, String executorParam, String addressList) {
        // force cover job param
        if (executorParam == null) {
            executorParam = "";
        }

        JobTriggerPoolHelper.trigger(jobId, TriggerTypeEnum.MANUAL, -1, null, executorParam, addressList);
        return Result.success();
    }

    @GetMapping("/nextTriggerTime")
    public Result nextTriggerTime(String scheduleType, String scheduleConf) {

        XxlJobInfo paramXxlJobInfo = new XxlJobInfo();
        paramXxlJobInfo.setScheduleType(scheduleType);
        paramXxlJobInfo.setScheduleConf(scheduleConf);

        List<String> result = new ArrayList<>();
        try {
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = JobScheduleHelper.generateNextValidTime(paramXxlJobInfo, lastTime);
                if (lastTime != null) {
                    result.add(DateUtil.formatDateTime(lastTime));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.failure("500", (I18nUtil.getString("schedule_type") + I18nUtil.getString("system_unvalid")) + e.getMessage());
        }
        return Result.success(result);

    }

}
