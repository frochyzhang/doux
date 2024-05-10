package cn.lezoo.doux.framework.conn.driver;

import cn.lezoo.doux.framework.extension.annotation.Extensible;

/**
 * @Description: 连接检查接口，用户需要自定义ping的校验逻辑
 * @Author: qipeng
 * @Date: 2022/7/1
 **/
@Extensible
public interface PingService {

    /**
     * 自定义连接检验逻辑
     *
     * @param connection 连接
     * @param request    请求报文
     * @param answer     标准响应，无需标准响应时可为null
     * @param spendTime  连接检查耗费的时间
     * @return 连接检查结果，连接正常返回true，否则返回异常
     */
    boolean pingConnection(Connection connection, String request, String answer, int spendTime);

//    /**
//     * 带重试机制的连接检查
//     *
//     * @param connection 连接
//     * @param request    请求报文
//     * @param answer     标准响应，无需标准响应时可为null
//     *                   * @param spendTime  连接检查耗费的时间
//     * @param retryTimes 重试次数
//     * @param interval   重试时间间隔
//     * @return 连接检查结果，连接正常返回true，否则返回异常
//     */
//    default boolean pingConnectionWithRetry(Connection connection, String request, String answer, int spendTime, int retryTimes, int interval) throws InterruptedException {
//
//    }
}
