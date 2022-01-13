package com.allinfinance.dev.xxl.job.admin.dto;

import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobInfo;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobLogGlue;

import java.util.List;

/**
 * @author huanghf
 * @date 2022/1/4 14:51
 */
public class JobGlueIndexInfoResponseDTO {
    /**
     * Glue类型-字典
     */
    private List<String> glueTypeList;
    /**
     * 任务信息
     */
    private XxlJobInfo jobInfo;
    /**
     * jobGlue编辑历史
     */
    private List<XxlJobLogGlue> jobLogGlues;

    public List<String> getGlueTypeList() {
        return glueTypeList;
    }

    public void setGlueTypeList(List<String> glueTypeList) {
        this.glueTypeList = glueTypeList;
    }

    public XxlJobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(XxlJobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    public List<XxlJobLogGlue> getJobLogGlues() {
        return jobLogGlues;
    }

    public void setJobLogGlues(List<XxlJobLogGlue> jobLogGlues) {
        this.jobLogGlues = jobLogGlues;
    }

    @Override
    public String toString() {
        return "JobGlueIndexInfoResponseDTO{" +
                "glueTypeList=" + glueTypeList +
                ", jobInfo=" + jobInfo +
                ", jobLogGlues=" + jobLogGlues +
                '}';
    }
}
