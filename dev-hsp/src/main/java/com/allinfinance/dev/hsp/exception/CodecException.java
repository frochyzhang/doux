package com.allinfinance.dev.hsp.exception;

/**
 * Classname  com.allinfinance.dev.hsp.exception.CodecException
 *
 * @Description TODO
 * @Date 2021/3/25 9:31
 * @Created by ZhangYong
 */

public class CodecException extends Exception {
    public CodecException() {
    }

    public CodecException(String msg) {
        super(msg);
    }

    public CodecException(Throwable cause) {
        super(cause);
    }

    public CodecException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

