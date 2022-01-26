package com.allinfinance.dev.actuator.model;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/1/21 16:48
 */
public class Greeting {
    private String msg;

    public Greeting() {
        this.msg = "lasdjkfalskdflasdk";
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "Greeting{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
