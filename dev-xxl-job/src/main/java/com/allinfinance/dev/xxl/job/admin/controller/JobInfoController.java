package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.common.dictionary.result.Result;
import com.allinfinance.dev.common.dictionary.result.ResultCodeEnum;
import com.allinfinance.dev.xxl.job.admin.config.ExecutorBlockStrategyEnum;
import com.allinfinance.dev.xxl.job.admin.constant.XxlJobResultCodeEnum;
import com.allinfinance.dev.xxl.job.admin.core.cron.CronExpression;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobGroup;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobInfo;
import com.allinfinance.dev.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.allinfinance.dev.xxl.job.admin.core.scheduler.MisfireStrategyEnum;
import com.allinfinance.dev.xxl.job.admin.core.scheduler.ScheduleTypeEnum;
import com.allinfinance.dev.xxl.job.admin.core.thread.JobScheduleHelper;
import com.allinfinance.dev.xxl.job.admin.core.thread.JobTriggerPoolHelper;
import com.allinfinance.dev.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobGroupDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobInfoDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobLogDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobLogGlueDao;
import com.allinfinance.dev.xxl.job.admin.dto.JobInfoIndexResponseDTO;
import com.github.pagehelper.PageInfo;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Api(value = "JobInfoController", tags = {"任务管理接口"})
@RestController
@RequestMapping("/job/jobs")
public class JobInfoController {
    private static final Logger logger = LoggerFactory.getLogger(JobInfoController.class);

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    private XxlJobLogDao xxlJobLogDao;
    @Resource
    private XxlJobLogGlueDao xxlJobLogGlueDao;

    @GetMapping("/constants")
    @ApiOperation("查询任务管理页面需要的常量数据")
    public Result index() {
        logger.info("查询任务管理页面需要的常量数据");
        JobInfoIndexResponseDTO jobInfoIndexResponseDTO = new JobInfoIndexResponseDTO();
        // 枚举-字典
        // 路由策略-列表
        jobInfoIndexResponseDTO.setExecutorRouteStrategyList(Arrays.stream(ExecutorRouteStrategyEnum.values())
                .map(ExecutorRouteStrategyEnum::getTitle).collect(Collectors.toList()));
        // Glue类型-字典
        jobInfoIndexResponseDTO.setGlueTypeList(Arrays.stream(GlueTypeEnum.values()).map(GlueTypeEnum::getDesc)
                .collect(Collectors.toList()));
        // 阻塞处理策略-字典
        jobInfoIndexResponseDTO.setExecutorBlockStrategyList(Arrays.stream(ExecutorBlockStrategyEnum.values())
                .map(ExecutorBlockStrategyEnum::getTitle).collect(Collectors.toList()));
        // 调度类型
        jobInfoIndexResponseDTO.setScheduleTypeList(Arrays.stream(ScheduleTypeEnum.values())
                .map(ScheduleTypeEnum::getTitle).collect(Collectors.toList()));
        // 调度过期策略
        jobInfoIndexResponseDTO.setMisfireStrategyList(Arrays.stream(MisfireStrategyEnum.values())
                .map(MisfireStrategyEnum::getTitle).collect(Collectors.toList()));

        // 执行器列表
        // TODO: 2021/12/30 权限控制待定，返回所有执行器
        List<XxlJobGroup> xxlJobGroupDaoAll = xxlJobGroupDao.findAll();

        jobInfoIndexResponseDTO.setJobGroupList(xxlJobGroupDaoAll);
        logger.info("查询完成");
        logger.debug("执行器列表: {}", xxlJobGroupDaoAll);
        return Result.success(jobInfoIndexResponseDTO);
    }

    @GetMapping
    @ApiOperation("分页查询任务列表")
    public Result pageList(@RequestParam(name = "current") Integer pageNo,
                           @RequestParam(name = "pageSize") Integer pageSize,
                           @RequestParam(name = "jobGroupId", required = false) Integer jobGroupId,
                           @RequestParam(name = "triggerStatus", required = false) Integer triggerStatus,
                           @RequestParam(name = "jobDesc", required = false) String jobDesc,
                           @RequestParam(name = "executorHandler", required = false) String executorHandler,
                           @RequestParam(name = "author", required = false) String author) {
        logger.info("分页查询任务列表, jobGroupId: {}, triggerStatus: {}, jobDesc: {}, executorHandler: {}, author: {}", jobGroupId, triggerStatus, jobDesc, executorHandler, author);
        List<XxlJobInfo> xxlJobInfoList = xxlJobInfoDao.pageList((pageNo - 1) * pageSize, pageSize, jobGroupId, triggerStatus, jobDesc, executorHandler, author);
        int pageListCount = xxlJobInfoDao.pageListCount(jobGroupId, triggerStatus, jobDesc, executorHandler, author);
        PageInfo<XxlJobInfo> xxlJobInfoPageInfo = new PageInfo<>(xxlJobInfoList);
        xxlJobInfoPageInfo.setTotal(pageListCount);
        logger.info("查询任务列表完成");
        logger.debug("任务列表: {}", xxlJobInfoList);
        return Result.success(xxlJobInfoPageInfo);
    }

    @GetMapping("/groups/{jobGroupId}")
    @ApiOperation("根据groupId查询对应的任务列表")
    public Result getJobsByGroup(@PathVariable int jobGroupId) {
        logger.info("根据jobGroupId查询对应的任务列表, jobGroupId: {}", jobGroupId);
        // jobGroup change, job list init and select
        List<XxlJobInfo> xxlJobInfoList = xxlJobInfoDao.getJobsByGroup(jobGroupId);
        logger.info("根据jobGroupId查询对应的任务列表完成");
        logger.debug("任务列表: {}", xxlJobInfoList);
        return Result.success(xxlJobInfoList);
    }

    @PostMapping
    @ApiOperation("新增任务信息")
    public Result add(@RequestBody @Valid XxlJobInfo jobInfo) {
        logger.info("新增任务信息: {}", jobInfo);

        XxlJobGroup xxlJobGroup = xxlJobGroupDao.load(jobInfo.getJobGroup());
        if (xxlJobGroup == null) {
            logger.error("执行器信息不存在, jobGroupId: {}", jobInfo.getJobGroup());
            return Result.failure(XxlJobResultCodeEnum.JOB_GROUP_NOT_EXIST);
        }

        // valid trigger
        ScheduleTypeEnum scheduleType = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
        if (scheduleType == ScheduleTypeEnum.CRON) {
            if (!CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
                logger.error("CRON表达式格式不正确, cronExpression: {}", jobInfo.getScheduleConf());
                return Result.failure(XxlJobResultCodeEnum.CRON_EXPRESSION_INVALID);
            }
        } else if (scheduleType == ScheduleTypeEnum.FIX_RATE) {
            int fixSecond = Integer.parseInt(jobInfo.getScheduleConf());
            if (fixSecond < 1) {
                logger.error("调度类型为固定速率时速率不能小于1秒");
                return Result.failure(XxlJobResultCodeEnum.FIX_RATE_LESS_THAN_ONE_SECOND);
            }
        } else {
            jobInfo.setScheduleConf(null);
        }

        // valid job
        GlueTypeEnum glueType = GlueTypeEnum.match(jobInfo.getGlueType());
        if (glueType == GlueTypeEnum.BEAN) {
            if (StringUtils.isBlank(jobInfo.getExecutorHandler())) {
                logger.error("运行模式为BEAN模式时jobHandler不能为空");
                return Result.failure(XxlJobResultCodeEnum.BEAN_JOB_HANDLER_IS_BLANK);
            }
        } else {
            jobInfo.setExecutorHandler(null);
        }

        // 》ChildJobId valid
        if (StringUtils.isNotBlank(jobInfo.getChildJobId())) {
            String[] childJobIds = jobInfo.getChildJobId().split(",");
            for (String childJobId : childJobIds) {
                if (StringUtils.isNotBlank(childJobId) && StringUtils.isNumeric(childJobId)) {
                    XxlJobInfo childJobInfo = xxlJobInfoDao.loadById(Integer.parseInt(childJobId));
                    if (childJobInfo == null) {
                        logger.error("子任务不存在, jobgId: {}", childJobId);
                        return Result.failure(XxlJobResultCodeEnum.CHILD_JOB_INFO_NOT_EXIST);
                    }
                } else {
                    logger.error("子任务ID格式不正确, childJobId: {}", jobInfo.getChildJobId());
                    return Result.failure(XxlJobResultCodeEnum.CHILD_JOB_INFO_ID_INVALID);
                }
            }
        }

        // add in db
        jobInfo.setAddTime(new Date());
        jobInfo.setUpdateTime(new Date());
        jobInfo.setGlueUpdatetime(new Date());
        xxlJobInfoDao.save(jobInfo);
        if (jobInfo.getId() < 1) {
            logger.error("保存任务信息失败");
            return Result.failure(XxlJobResultCodeEnum.JOB_INFO_SAVE_FAILED);
        }

        logger.info("新增任务信息完成");
        return Result.success();
    }

    @PutMapping
    @ApiOperation("更新任务信息")
    public Result update(@RequestBody @Valid XxlJobInfo jobInfo) {
        logger.info("更新任务信息: {}", jobInfo);
        XxlJobGroup xxlJobGroup = xxlJobGroupDao.load(jobInfo.getJobGroup());
        if (xxlJobGroup == null) {
            logger.error("执行器信息不存在, jobGroupId: {}", jobInfo.getJobGroup());
            return Result.failure(XxlJobResultCodeEnum.JOB_GROUP_NOT_EXIST);
        }

        // valid
        ScheduleTypeEnum scheduleType = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
        if (scheduleType == ScheduleTypeEnum.CRON) {
            if (!CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
                logger.error("CRON表达式格式不正确, cronExpression: {}", jobInfo.getScheduleConf());
                return Result.failure(XxlJobResultCodeEnum.CRON_EXPRESSION_INVALID);
            }
        } else if (scheduleType == ScheduleTypeEnum.FIX_RATE) {
            int fixSecond = Integer.parseInt(jobInfo.getScheduleConf());
            if (fixSecond < 1) {
                logger.error("调度类型为固定速率时速率不能小于1秒");
                return Result.failure(XxlJobResultCodeEnum.FIX_RATE_LESS_THAN_ONE_SECOND);
            }
        }

        // valid job
        GlueTypeEnum glueType = GlueTypeEnum.match(jobInfo.getGlueType());
        if (glueType == GlueTypeEnum.BEAN) {
            if (StringUtils.isBlank(jobInfo.getExecutorHandler())) {
                logger.error("运行模式为BEAN模式时jobHandler不能为空");
                return Result.failure(XxlJobResultCodeEnum.BEAN_JOB_HANDLER_IS_BLANK);
            }
        }

        // 》ChildJobId valid
        if (StringUtils.isNotBlank(jobInfo.getChildJobId())) {
            String[] childJobIds = jobInfo.getChildJobId().split(",");
            for (String childJobId : childJobIds) {
                if (StringUtils.isNotBlank(childJobId) && StringUtils.isNumeric(childJobId)) {
                    XxlJobInfo childJobInfo = xxlJobInfoDao.loadById(Integer.parseInt(childJobId));
                    if (childJobInfo == null) {
                        logger.error("子任务不存在, jobgId: {}", childJobId);
                        return Result.failure(XxlJobResultCodeEnum.CHILD_JOB_INFO_NOT_EXIST);
                    }
                } else {
                    logger.error("子任务ID格式不正确, childJobId: {}", jobInfo.getChildJobId());
                    return Result.failure(XxlJobResultCodeEnum.CHILD_JOB_INFO_ID_INVALID);
                }
            }
        }

        // stage job info
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(jobInfo.getId());
        if (xxlJobInfo == null) {
            logger.error("要更新的job信息不存在, jobId: {}", jobInfo.getId());
            return Result.failure(XxlJobResultCodeEnum.JOB_INFO_NOT_EXIST);
        }

        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = xxlJobInfo.getTriggerNextTime();
        boolean scheduleDataNotChanged = jobInfo.getScheduleType().equals(xxlJobInfo.getScheduleType()) && jobInfo.getScheduleConf().equals(xxlJobInfo.getScheduleConf());
        if (xxlJobInfo.getTriggerStatus() == 1 && !scheduleDataNotChanged) {
            try {
                Date nextValidTime = JobScheduleHelper.generateNextValidTime(jobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
                if (nextValidTime == null) {
                    logger.error("调度类型不合法");
                    return Result.failure(XxlJobResultCodeEnum.SCHEDULE_TYPE_INVALID);
                }
                nextTriggerTime = nextValidTime.getTime();
            } catch (Exception e) {
                logger.error("获取nextValidTime异常", e);
                return Result.failure(XxlJobResultCodeEnum.SCHEDULE_TYPE_INVALID);
            }
        }

        xxlJobInfo.setJobGroup(jobInfo.getJobGroup());
        xxlJobInfo.setJobDesc(jobInfo.getJobDesc());
        xxlJobInfo.setAuthor(jobInfo.getAuthor());
        xxlJobInfo.setAlarmEmail(jobInfo.getAlarmEmail());
        xxlJobInfo.setScheduleType(jobInfo.getScheduleType());
        xxlJobInfo.setScheduleConf(jobInfo.getScheduleConf());
        xxlJobInfo.setMisfireStrategy(jobInfo.getMisfireStrategy());
        xxlJobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
        xxlJobInfo.setExecutorHandler(jobInfo.getExecutorHandler());
        xxlJobInfo.setExecutorParam(jobInfo.getExecutorParam());
        xxlJobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        xxlJobInfo.setExecutorTimeout(jobInfo.getExecutorTimeout());
        xxlJobInfo.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
        xxlJobInfo.setChildJobId(jobInfo.getChildJobId());
        xxlJobInfo.setTriggerNextTime(nextTriggerTime);

        xxlJobInfo.setUpdateTime(new Date());
        xxlJobInfoDao.update(xxlJobInfo);

        logger.info("更新任务信息完成");
        return Result.success();
    }

    @DeleteMapping("{jobId}")
    @ApiOperation("删除任务信息")
    public Result remove(@PathVariable int jobId) {
        logger.info("删除任务信息, jobId: {}", jobId);
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(jobId);
        if (xxlJobInfo == null) {
            return Result.success();
        }

        xxlJobInfoDao.delete(jobId);
        xxlJobLogDao.delete(jobId);
        xxlJobLogGlueDao.deleteByJobId(jobId);
        logger.info("删除任务信息完成");
        return Result.success();
    }

    @PutMapping("{jobId}/start")
    @ApiOperation("启动任务")
    public Result start(@PathVariable int jobId) {
        logger.info("启动任务, jobId: {}", jobId);
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(jobId);

        // valid
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(xxlJobInfo.getScheduleType(), ScheduleTypeEnum.NONE);
        if (ScheduleTypeEnum.NONE == scheduleTypeEnum) {
            logger.error("调度类型为无时禁止启动");
            return Result.failure(XxlJobResultCodeEnum.CURRENT_SCHEDULE_TYPE_LIMIT_START);
        }

        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = 0;
        try {
            Date nextValidTime = JobScheduleHelper.generateNextValidTime(xxlJobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
            if (nextValidTime == null) {
                logger.error("调度类型不合法");
                return Result.failure(XxlJobResultCodeEnum.SCHEDULE_TYPE_INVALID);
            }
            nextTriggerTime = nextValidTime.getTime();
        } catch (Exception e) {
            logger.error("获取nextValidTime异常", e);
            return Result.failure(XxlJobResultCodeEnum.SCHEDULE_TYPE_INVALID);
        }

        xxlJobInfo.setTriggerStatus(1);
        xxlJobInfo.setTriggerLastTime(0);
        xxlJobInfo.setTriggerNextTime(nextTriggerTime);

        xxlJobInfo.setUpdateTime(new Date());
        xxlJobInfoDao.update(xxlJobInfo);

        logger.info("启动任务完成");
        return Result.success();
    }

    @PutMapping("{jobId}/stop")
    @ApiOperation("暂停任务")
    public Result pause(@PathVariable int jobId) {
        logger.info("暂停任务, jobId: {}", jobId);
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(jobId);
        if (xxlJobInfo == null) {
            logger.error("任务信息不存在, jobId: {}", jobId);
            return Result.failure(XxlJobResultCodeEnum.JOB_INFO_NOT_EXIST);
        }

        xxlJobInfo.setTriggerStatus(0);
        xxlJobInfo.setTriggerLastTime(0);
        xxlJobInfo.setTriggerNextTime(0);

        xxlJobInfo.setUpdateTime(new Date());
        xxlJobInfoDao.update(xxlJobInfo);

        logger.info("暂停任务完成");
        return Result.success();
    }

    @PutMapping("{jobId}/trigger")
    @ApiOperation("执行一次任务")
    public Result triggerJob(@PathVariable int jobId,
                             @RequestParam(name = "executorParam", required = false) String executorParam,
                             @RequestParam(name = "addressList", required = false) String addressList) {
        logger.info("执行一次任务, jobId: {}, 任务参数: {}, 机器地址列表: {}", jobId, executorParam, addressList);
        // force cover job param
        if (executorParam == null) {
            executorParam = "";
        }

        JobTriggerPoolHelper.trigger(jobId, TriggerTypeEnum.MANUAL, -1, null, executorParam, addressList);
        logger.info("执行一次任务完成");
        return Result.success();
    }

    @GetMapping("/nextTriggerTime")
    @ApiOperation("下次执行时间")
    public Result nextTriggerTime(@RequestParam(name = "scheduleType") String scheduleType,
                                  @RequestParam(name = "scheduleConf") String scheduleConf) {
        logger.info("查询任务下次执行时间, 调度类型: {}, 调度配置: {}", scheduleType, scheduleConf);
        XxlJobInfo paramXxlJobInfo = new XxlJobInfo();
        paramXxlJobInfo.setScheduleType(scheduleType);
        paramXxlJobInfo.setScheduleConf(scheduleConf);

        List<String> nextTriggerTimeList = new ArrayList<>();
        try {
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = JobScheduleHelper.generateNextValidTime(paramXxlJobInfo, lastTime);
                if (lastTime != null) {
                    nextTriggerTimeList.add(DateUtil.formatDateTime(lastTime));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("计算下次执行时间异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("下次执行时间列表: {}", nextTriggerTimeList);
        return Result.success(nextTriggerTimeList);
    }
}
