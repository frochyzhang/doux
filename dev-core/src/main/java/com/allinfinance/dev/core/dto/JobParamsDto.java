package com.allinfinance.dev.core.dto;

/**
 * @author 张勇
 * @description
 * @date 2020/12/24 10:45
 */
public class JobParamsDto {
    private String paramName;
    private String paramValue;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public JobParamsDto(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    @Override
    public String toString() {
        return "JobParamsDto{" +
                "paramName='" + paramName + '\'' +
                ", paramValue='" + paramValue + '\'' +
                '}';
    }
}
