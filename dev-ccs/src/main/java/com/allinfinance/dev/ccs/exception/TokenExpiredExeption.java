package com.allinfinance.dev.ccs.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * token 过期异常
 */
public class TokenExpiredExeption extends AuthenticationException {

    public TokenExpiredExeption(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TokenExpiredExeption(String msg) {
        super(msg);
    }
}
