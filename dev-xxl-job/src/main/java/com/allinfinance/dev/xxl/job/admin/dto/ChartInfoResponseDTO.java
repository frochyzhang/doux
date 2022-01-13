package com.allinfinance.dev.xxl.job.admin.dto;

import java.util.List;

/**
 * @author huanghf
 * @date 2021/12/29 15:56
 */
public class ChartInfoResponseDTO {
    /**
     * 调度次数信息的集合
     */
    private List<TriggerDayInfo> triggerDayList;
    /**
     * 进行中的调度总数量
     */
    private int triggerCountRunningTotal;
    /**
     * 成功的调度总数量
     */
    private int triggerCountSucTotal;
    /**
     * 失败的调度总数量
     */
    private int triggerCountFailTotal;

    public List<TriggerDayInfo> getTriggerDayList() {
        return triggerDayList;
    }

    public void setTriggerDayList(List<TriggerDayInfo> triggerDayList) {
        this.triggerDayList = triggerDayList;
    }

    public int getTriggerCountRunningTotal() {
        return triggerCountRunningTotal;
    }

    public void setTriggerCountRunningTotal(int triggerCountRunningTotal) {
        this.triggerCountRunningTotal = triggerCountRunningTotal;
    }

    public int getTriggerCountSucTotal() {
        return triggerCountSucTotal;
    }

    public void setTriggerCountSucTotal(int triggerCountSucTotal) {
        this.triggerCountSucTotal = triggerCountSucTotal;
    }

    public int getTriggerCountFailTotal() {
        return triggerCountFailTotal;
    }

    public void setTriggerCountFailTotal(int triggerCountFailTotal) {
        this.triggerCountFailTotal = triggerCountFailTotal;
    }

    @Override
    public String toString() {
        return "ChartInfoResponseDTO{" +
                "triggerDayList=" + triggerDayList +
                ", triggerCountRunningTotal=" + triggerCountRunningTotal +
                ", triggerCountSucTotal=" + triggerCountSucTotal +
                ", triggerCountFailTotal=" + triggerCountFailTotal +
                '}';
    }
}
