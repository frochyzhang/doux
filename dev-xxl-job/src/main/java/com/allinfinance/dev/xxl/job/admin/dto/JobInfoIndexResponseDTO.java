package com.allinfinance.dev.xxl.job.admin.dto;

import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobGroup;

import java.util.List;

/**
 * @author huanghf
 * @date 2021/12/30 16:26
 */
public class JobInfoIndexResponseDTO {
    /**
     * 路由策略-列表
     */
    private List<String> executorRouteStrategyList;
    /**
     * Glue类型-字典
     */
    private List<String> glueTypeList;
    /**
     * 阻塞处理策略-字典
     */
    private List<String> executorBlockStrategyList;
    /**
     * 调度类型
     */
    private List<String> scheduleTypeList;
    /**
     * 调度过期策略
     */
    private List<String> misfireStrategyList;
    /**
     * 执行器列表
     */
    private List<XxlJobGroup> JobGroupList;

    public List<String> getExecutorRouteStrategyList() {
        return executorRouteStrategyList;
    }

    public void setExecutorRouteStrategyList(List<String> executorRouteStrategyList) {
        this.executorRouteStrategyList = executorRouteStrategyList;
    }

    public List<String> getGlueTypeList() {
        return glueTypeList;
    }

    public void setGlueTypeList(List<String> glueTypeList) {
        this.glueTypeList = glueTypeList;
    }

    public List<String> getExecutorBlockStrategyList() {
        return executorBlockStrategyList;
    }

    public void setExecutorBlockStrategyList(List<String> executorBlockStrategyList) {
        this.executorBlockStrategyList = executorBlockStrategyList;
    }

    public List<String> getScheduleTypeList() {
        return scheduleTypeList;
    }

    public void setScheduleTypeList(List<String> scheduleTypeList) {
        this.scheduleTypeList = scheduleTypeList;
    }

    public List<String> getMisfireStrategyList() {
        return misfireStrategyList;
    }

    public void setMisfireStrategyList(List<String> misfireStrategyList) {
        this.misfireStrategyList = misfireStrategyList;
    }

    public List<XxlJobGroup> getJobGroupList() {
        return JobGroupList;
    }

    public void setJobGroupList(List<XxlJobGroup> jobGroupList) {
        JobGroupList = jobGroupList;
    }

    @Override
    public String toString() {
        return "JobInfoIndexResponseDTO{" +
                "executorRouteStrategyList=" + executorRouteStrategyList +
                ", glueTypeList=" + glueTypeList +
                ", executorBlockStrategyList=" + executorBlockStrategyList +
                ", scheduleTypeList=" + scheduleTypeList +
                ", misfireStrategyList=" + misfireStrategyList +
                ", JobGroupList=" + JobGroupList +
                '}';
    }
}
