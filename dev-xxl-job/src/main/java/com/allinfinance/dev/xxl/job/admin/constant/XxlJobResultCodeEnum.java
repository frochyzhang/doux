package com.allinfinance.dev.xxl.job.admin.constant;

import com.allinfinance.dev.core.util.result.ResultCode;

/**
 * @author huanghf
 * @date 2021/12/30 15:45
 */
public enum XxlJobResultCodeEnum implements ResultCode {
    APP_NAME_INVALID("500", "appName格式不正确"),
    TITLE_IS_INVALID("500", "title格式不正确"),
    ADDRESS_LIST_INVALID("500", "执行器机器地址列表格式不正确"),
    ADDRESS_LIST_EMPTY("500", "执行器机器地址列表为空"),
    JOB_INFO_NOT_EMPTY("500", "删除JobGroup需要对应JobInfo不存在"),
    JOB_GROUP_NOT_EXIST("500", "选择的执行器不存在"),
    CRON_EXPRESSION_INVALID("500", "CRON表达式格式不正确"),
    FIX_RATE_LESS_THAN_ONE_SECOND("500", "固定速度不能小于1秒"),
    JOB_INFO_NOT_EXIST("500", "任务不存在"),
    CHILD_JOB_INFO_NOT_EXIST("500", "子任务不存在"),
    CHILD_JOB_INFO_ID_INVALID("500", "子任务ID格式不正确"),
    JOB_INFO_SAVE_FAILED("500", "任务信息保存失败"),
    SCHEDULE_TYPE_INVALID("500", "调度类型不合法"),
    CURRENT_SCHEDULE_TYPE_LIMIT_START("500", "当前调度类型禁止启动"),
    JOB_LOG_NOT_EXIST("500", "任务调度日志不存在"),
    JOB_LOG_KILL_DISPATCH_FAILED("500", "调度失败，无法终止日志"),
    JOB_LOG_KILL_FAILED("500", "终止日志失败"),
    INVALID_CLEAR_LOG_TYPE("500", "不合法的日志清理类型参数"),
    JOB_GLUE_TYPE_INVALID("500", "该任务不是GLUE模式"),
    GLUE_REMARK_LENGTH_LIMIT("500", "源码备注长度限制为4~100"),
    BEAN_JOB_HANDLER_IS_BLANK("500", "运行模式为BEAN模式时jobHandler不能为空");

    private final String code;
    private final String message;

    XxlJobResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
