package com.allinfinance.dev.xxl.job.admin.dto;

/**
 * @author huanghf
 * @date 2021/12/29 15:36
 */
public class IndexInfoResponseDTO {
    /**
     * 调度中心运行的任务数量
     */
    private int jobInfoCount;
    /**
     * 调度中心触发的调度次数
     */
    private int jobLogCount;
    /**
     * 调度中心触发的调度成功次数
     */
    private int jobLogSuccessCount;
    /**
     * 调度中心在线的执行器机器数量
     */
    private int executorCount;

    public int getJobInfoCount() {
        return jobInfoCount;
    }

    public void setJobInfoCount(int jobInfoCount) {
        this.jobInfoCount = jobInfoCount;
    }

    public int getJobLogCount() {
        return jobLogCount;
    }

    public void setJobLogCount(int jobLogCount) {
        this.jobLogCount = jobLogCount;
    }

    public int getJobLogSuccessCount() {
        return jobLogSuccessCount;
    }

    public void setJobLogSuccessCount(int jobLogSuccessCount) {
        this.jobLogSuccessCount = jobLogSuccessCount;
    }

    public int getExecutorCount() {
        return executorCount;
    }

    public void setExecutorCount(int executorCount) {
        this.executorCount = executorCount;
    }

    @Override
    public String toString() {
        return "IndexInfoResponseDTO{" +
                "jobInfoCount=" + jobInfoCount +
                ", jobLogCount=" + jobLogCount +
                ", jobLogSuccessCount=" + jobLogSuccessCount +
                ", executorCount=" + executorCount +
                '}';
    }
}
