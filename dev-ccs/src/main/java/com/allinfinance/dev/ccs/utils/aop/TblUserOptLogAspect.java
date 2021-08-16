package com.allinfinance.dev.ccs.utils.aop;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.controller.OptLogController;
import com.allinfinance.dev.ccs.dal.model.TblUserErrorLog;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.service.TblOptLogService;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.IpAddressUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/7/19 10:48
 * @description：切面处理类，操作日志异常日志记录处理
 */

@Aspect
@Component
public class TblUserOptLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(TblUserOptLogAspect.class);

    @Autowired
    private TblOptLogService tblOptLogService;

//    @Autowired
//    private TblUserErrorLogService exceptionLogService;

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.allinfinance.dev.ccs.utils.annotation.OperLog)")
    public void operLogPoinCut() {
    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(* com.allinfinance.dev.ccs.controller..*.*(..))")
    public void operTblUserErrorLogPoinCut() {
    }


    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveTblUserOptLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        TblUserOptLog operlog = new TblUserOptLog();
//            operlog.setOperId(UuidUtil.get32UUID()); // 主键ID

        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        OperLog opLog = method.getAnnotation(OperLog.class);
        if (opLog != null) {
            String operModul = opLog.operModul();
            String operType = opLog.operType();
            String operDesc = opLog.operDesc();
            operlog.setOperModule(operModul); // 操作模块
            operlog.setOperType(operType); // 操作类型
            operlog.setOperDesc(operDesc); // 操作描述
        }
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;

        operlog.setOperMethod(methodName); // 请求方法

        // 请求的参数
        if (request != null) {
            try {
                Map<String, String> rtnMap = converMap(request.getParameterMap());
                // 将参数所在的数组转换成json
                String params = JSON.toJSONString(rtnMap);
                // 这里没有参数就检查是不是因为@RequestBody作为请求参数，调用专门的工具类取解析这样的请求参数
                if (StringUtils.equals(params, "{}")) {
                    Object[] args = joinPoint.getArgs();
                    params = ObjectMapUtil.getParameterValue(args);
                }
                operlog.setOperRequParam(params); // 请求参数
//            operlog.setOperRespParam(JSON.toJSONString(keys)); // 返回结果
                // 系统登录时用户的额
                operlog.setOperUserId(JwtUtil.getUserId(request.getHeader(AosContent.AOS_TOKEN))); // 请求用户ID
                operlog.setOperUserName(JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN))); // 请求用户名称
                operlog.setOrg(JwtUtil.getOrg(request.getHeader(AosContent.AOS_TOKEN))); // 请求用户机构
                operlog.setOperIp(IpAddressUtil.getIpAddress(request)); // 请求IP
                operlog.setOperUri(request.getRequestURI()); // 请求URI
                operlog.setOperCreateTime(new Date()); // 创建时间
            } catch (Exception e) {
                logger.error("操作日志参数获取异常：{}", e.getMessage());
            }
        }
        // 实际使用中因为查询操作太多 所以剔除查询日志
        try {
            if (!(operlog.getOperType().equals(AosContent.QUERY))) {
                tblOptLogService.insertLog(operlog);
            }
        } catch (Exception e) {
            logger.error("操作日志INSERT异常-->日志信息{}", operlog.toString());
        }
    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "operTblUserErrorLogPoinCut()", throwing = "e")
    public void saveTblUserErrorLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        TblUserErrorLog excepLog = new TblUserErrorLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
//            excepLog.setExcId(UuidUtil.get32UUID());
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);
            excepLog.setExcRequParam(params); // 请求参数
            excepLog.setOperMethod(methodName); // 请求方法名
            excepLog.setExcName(e.getClass().getName()); // 异常名称
            excepLog.setExcMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace())); // 异常信息
            excepLog.setOperUserId(JwtUtil.getUserId(request.getHeader(AosContent.AOS_TOKEN))); // 操作员ID
            excepLog.setOperUserName(JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN))); // 操作员名称
            excepLog.setOrg(JwtUtil.getOrg(request.getHeader(AosContent.AOS_TOKEN))); // 操作员名称
            excepLog.setOperUri(request.getRequestURI()); // 操作URI
            excepLog.setOperIp(null); // 操作员IP
            excepLog.setOperCreateTime(new Date()); // 发生异常时间

//            exceptionLogService.insert(excepLog);

        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }
}
