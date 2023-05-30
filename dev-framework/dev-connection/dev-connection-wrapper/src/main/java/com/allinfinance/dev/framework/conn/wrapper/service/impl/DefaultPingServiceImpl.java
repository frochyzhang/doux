package com.allinfinance.dev.framework.conn.wrapper.service.impl;

import com.allinfinance.dev.framework.conn.driver.Connection;
import com.allinfinance.dev.framework.conn.driver.PingService;
import com.allinfinance.dev.framework.extension.annotation.Extension;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/1
 **/
@Extension("default")
public class DefaultPingServiceImpl implements PingService {

    /**
     * 自定义连接检验逻辑
     *
     * @param connection 连接
     * @param request    请求报文
     * @param answer     标准响应，无需标准响应时可为null
     * @param spendTime  连接检查耗费的时间
     * @return 连接检查结果，连接正常返回true，否则返回异常
     */
    @Override
    public boolean pingConnection(Connection connection, String request, String answer, int spendTime) {
        try {
            long startTime = System.currentTimeMillis();
            String response = connection.send(request);
            long endTime = System.currentTimeMillis();
            if (StringUtils.isNotBlank(answer)) {
                //标准相应结果result不为空时进行ping相应的校验
                return answer.equals(response) && (endTime - startTime) <= spendTime;
            } else {
                //标准相应结果为空时，仅需要服务端相应不为空且在目标时间内即可
                return ObjectUtils.allNotNull(response) && (endTime - startTime) <= spendTime;
            }
        } catch (Throwable e) {
            return false;
        }
    }
}
