package com.allinfinance.dev.ccs.result;

import java.util.Arrays;

/**
 * @author 张勇
 * @description 状态返回吗及描述
 * @date 2020/5/17 02:07
 */
public enum ResultCodeEnum implements ResultCode {
    /*成功状态码*/
    SUCCESS(0, "成功"),
    SYSTEM_ERROR(400, "系统繁忙，请稍后重试"),
    Unauthorized(401, "未授权，请联系管理员！"),
    /*参数错误: 1001-1999 */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    OLD_USER_PASS_ERROR(1005, "原密码错误"),
    /*用户错误: 2001-2999*/
    USER_NOT_LOGGED_IN(2001, "用户未登录,访问的路径需要验证,请登录"),
    USER_LOGIN_ERROR(2002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(2003, "账号已被禁用"),
    USER_NOT_EXIST(2004, "用户不存在或者不可用"),
    USER_HAS_EXISTED(2005, "用户已存在"),
    USER_CREDENTIALS_EXPIRED(2006, "账户已过期"),
    USER_ACCOUNT_USE_BY_OTHERS(2007, "登录已过期，请重新登录！"),
    USER_ACCOUNT_ODEERROR(2008, "验证密码错误！"),
    /* 通用错误返回 */
    GENERIC_EXCEPTION(4000, "系统内部错误！"),
    NULL_POINT(4001, "空指针异常"),
    HTTP_CLIENT_ERROR(4002, "HTTP异常");

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }

    public static ResultCodeEnum valueFromCode(Integer code) {
        return Arrays.stream(values()).filter(enu -> enu.code.equals(code))
                .findAny().orElse(GENERIC_EXCEPTION);
    }
}
