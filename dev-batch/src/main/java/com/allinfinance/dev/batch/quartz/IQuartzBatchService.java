package com.allinfinance.dev.batch.quartz;

import org.quartz.CronTrigger;

import java.util.HashMap;

/**
 * IQuartzBatchService
 *
 * @author hongmr
 * @date 2017/1/6
 */
public interface IQuartzBatchService {
    /**
     * 新增计划任务
     * @param jobName 任务名
     * @param jobGroupName 任务组名
     * @param jobClass 任务执行Bean
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param cronParam	cron表达式
     * @param jobParams job参数
     */
    public void addCronJob(String jobName, String jobGroupName, Class jobClass, String triggerName, String triggerGroupName, String cronParam, HashMap<String, String> jobParams);

    /**
     * 修改计划任务（执行时间）
     * 通过设置cron表达式完成，不重启trigger和job
     * @param trigger 触发器
     * @param newCronParam 新cron表达式
     */
    public void modifyCronJobTime(CronTrigger trigger, String newCronParam);

    /**
     * 移除计划任务
     * @param jobName 任务名
     * @param jobGroupName 任务组名
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     */
    public void removeCronJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName);

    /**
     * 启动所有计划任务
     */
    public void startCronJobs();

    /**
     * 关闭所有计划任务
     */
    public void shutdownCronJobs();

    /**
     * 获取触发器
     * @param triggerName 触发器名
     * @param triggerGroup 触发器组名
     * @return
     */
    public CronTrigger getTrigger(String triggerName, String triggerGroup);
}
