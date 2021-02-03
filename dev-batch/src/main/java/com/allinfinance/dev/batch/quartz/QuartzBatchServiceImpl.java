package com.allinfinance.dev.batch.quartz;

import com.allinfinance.dev.core.util.convert.common.ConvertUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;

/**
 * QuartzBatchServiceImpl
 *
 * @author hongmr
 * @date 2017/1/6
 */
public class QuartzBatchServiceImpl implements IQuartzBatchService {
    private final Logger logger = LoggerFactory.getLogger(QuartzBatchServiceImpl.class);
    private Scheduler scheduler;

    @Override
    public void addCronJob(String jobName,String jobGroupName, Class jobClass,String triggerName,String triggerGroupName,String cronParam,HashMap<String,String> jobParams) {
        try {
            JobDataMap jobDataMap = ConvertUtils.HashToJobData(jobParams);
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, jobGroupName)
                    .usingJobData(jobDataMap)
                    .build();
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(triggerName,triggerGroupName);
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronParam));
            CronTrigger trigger = (CronTrigger)triggerBuilder.build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("创建计划任务失败："+ triggerName);
            logger.error("异常信息:", e);
        }
        logger.info(new Date() + ": 新建" + triggerName + "计划任务");
    }


    @Override
    public void modifyCronJobTime(CronTrigger trigger,String newCronParam){
        try {
            if(trigger == null) {
                logger.warn("未找到相应触发器");
            } else if (!newCronParam.equalsIgnoreCase(trigger.getCronExpression())) {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(newCronParam);
                TriggerKey key = trigger.getKey();
                trigger = trigger.getTriggerBuilder().withIdentity(key)
                        .withSchedule(cronScheduleBuilder)
                        .build();
                scheduler.rescheduleJob(key, trigger);
                logger.info(new Date() + ": 更新" + trigger.getKey().getName() + "计划任务");
            }else {
                logger.info("计划任务时间相同，无需更新：" + trigger.getKey().getName());
            }
        } catch (SchedulerException e) {
            logger.error("更新计划任务失败：" + trigger.getKey().getName());
            logger.error("异常信息:", e);
        }
    }


    @Override
    public void removeCronJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName) {
        try {
            TriggerKey key = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(key);
            if(trigger == null) {
                logger.warn("未找到相应触发器："+ triggerName);
            } else {
                // 停止触发器
                scheduler.pauseTrigger(key);
                // 移除触发器
                scheduler.unscheduleJob(key);
                // 删除任务
                scheduler.deleteJob(JobKey.jobKey(key.getName(),key.getGroup()));
                logger.info(new Date() + ": 删除" + triggerName + "计划任务");
            }
        } catch (SchedulerException e) {
            logger.error("更新计划任务失败：" + triggerName);
            logger.error("异常信息:", e);
        }
    }


    @Override
    public void startCronJobs() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("异常信息:", e);
        }
        logger.info("启动所有定时任务成功.");
    }


    @Override
    public void shutdownCronJobs() {
        try {
            if(!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            logger.error("关闭所有定时任务失败.");
            logger.error("异常信息:", e);
        }
        logger.info("关闭所有定时任务成功.");
    }


    @Override
    public CronTrigger getTrigger(String triggerName, String triggerGroup) {
        try {
            return (CronTrigger) scheduler.getTrigger(
                    TriggerKey.triggerKey(triggerName, triggerGroup));
        } catch (SchedulerException e) {
            logger.error("异常信息:", e);
        }

        return null;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

}
