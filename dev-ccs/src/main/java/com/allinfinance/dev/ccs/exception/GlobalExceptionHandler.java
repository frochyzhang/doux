package com.allinfinance.dev.ccs.exception;

import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;


/**
 * @author 张勇
 * @description
 * @date 2020/5/17 02:26
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * -------- 通用异常处理方法 --------
     **/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.failure();    // 通用异常结果
    }

    /**
     * -------- 指定异常处理方法 --------
     **/
//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseBody
//    public Result errorA(AuthenticationException e) {
//        e.printStackTrace();
//        return Result.failure(ResultCodeEnum.Unauthorized);
//    }

    /**
     * -------- 指定异常处理方法 --------
     **/
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result error(NullPointerException e) {
        e.printStackTrace();
        return Result.failure(ResultCodeEnum.NULL_POINT);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public Result error(IndexOutOfBoundsException e) {
        e.printStackTrace();
        return Result.failure(ResultCodeEnum.HTTP_CLIENT_ERROR);
    }

    /**
     * -------- 自定义定异常处理方法 --------
     **/
    @ExceptionHandler({CustomException.class})
    @ResponseBody
    public Result error(CustomException e) {
        e.printStackTrace();
        return Result.failure(ResultCodeEnum.valueFromCode(e.getCode()));
    }
}
