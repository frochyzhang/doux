package com.allinfinance.dev.framework.conn.driver;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/6/29 14:19
 */
public interface ServerMetadata {
    Connection getConnection();

    Connection getConnection(String serverIp, Integer serverPort);
}
