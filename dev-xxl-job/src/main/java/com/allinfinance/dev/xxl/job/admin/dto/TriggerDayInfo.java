package com.allinfinance.dev.xxl.job.admin.dto;

/**
 * @author huanghf
 * @date 2021/12/29 16:03
 */
public class TriggerDayInfo {
    /**
     * 调度日期，yyyy-MM-dd
     */
    private String triggerDay;
    /**
     * 进行中的调度数量
     */
    private int triggerDayCountRunning;
    /**
     * 成功的调度数量
     */
    private int triggerDayCountSuc;
    /**
     * 失败的调度数量
     */
    private int triggerDayCountFail;

    public String getTriggerDay() {
        return triggerDay;
    }

    public void setTriggerDay(String triggerDay) {
        this.triggerDay = triggerDay;
    }

    public int getTriggerDayCountRunning() {
        return triggerDayCountRunning;
    }

    public void setTriggerDayCountRunning(int triggerDayCountRunning) {
        this.triggerDayCountRunning = triggerDayCountRunning;
    }

    public int getTriggerDayCountSuc() {
        return triggerDayCountSuc;
    }

    public void setTriggerDayCountSuc(int triggerDayCountSuc) {
        this.triggerDayCountSuc = triggerDayCountSuc;
    }

    public int getTriggerDayCountFail() {
        return triggerDayCountFail;
    }

    public void setTriggerDayCountFail(int triggerDayCountFail) {
        this.triggerDayCountFail = triggerDayCountFail;
    }

    @Override
    public String toString() {
        return "TriggerDayInfo{" +
                "triggerDay='" + triggerDay + '\'' +
                ", triggerDayCountRunning=" + triggerDayCountRunning +
                ", triggerDayCountSuc=" + triggerDayCountSuc +
                ", triggerDayCountFail=" + triggerDayCountFail +
                '}';
    }
}
