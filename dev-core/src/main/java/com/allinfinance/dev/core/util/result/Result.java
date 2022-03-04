package com.allinfinance.dev.core.util.result;

import java.io.Serializable;

/**
 * @author 张勇
 * @description 统一返回封装
 * @date 2020/5/17 02:12
 */
public class Result implements Serializable {

    private Boolean success;
    private String code;
    private String message;
    private Object data;
    private Integer status;

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public static Result success() {
        Result result = new Result();
        result.setSuccess(Boolean.TRUE);
        result.setResultCode(ResultCodeEnum.SUCCESS);
        result.setStatus(0);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(Boolean.TRUE);
        result.setResultCode(ResultCodeEnum.SUCCESS);
        result.setData(data);
        result.setStatus(0);
        return result;
    }

    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setSuccess(Boolean.FALSE);
        result.setCode(resultCode.code());
        result.setMessage(resultCode.message());
        result.setStatus(-1);
        return result;
    }

    public static Result failure() {
        Result result = new Result();
        result.setSuccess(Boolean.FALSE);
        result.setCode(ResultCodeEnum.GENERIC_EXCEPTION.code());
        result.setMessage(ResultCodeEnum.GENERIC_EXCEPTION.message());
        result.setStatus(-1);
        return result;
    }

    public static Result failure(String message, String code) {
        Result result = new Result();
        result.setSuccess(Boolean.FALSE);
        result.setCode(code);
        result.setStatus(-1);
        result.setMessage(message);
        return result;
    }


    public Boolean getSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", status=" + status +
                '}';
    }
}
