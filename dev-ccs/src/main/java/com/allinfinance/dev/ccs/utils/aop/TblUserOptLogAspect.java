package com.allinfinance.dev.ccs.utils.aop;

import com.alibaba.fastjson.JSON;
import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblUserErrorLog;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.service.TblOptLogService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.IpAddressUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/7/19 10:48
 * @description: 切面处理类，操作日志异常日志记录处理
 */

@Aspect
@Component
public class TblUserOptLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(TblUserOptLogAspect.class);

    @Value("${logType}")
    private String logType;

    @Autowired
    private TblOptLogService tblOptLogService;

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "@annotation(com.allinfinance.dev.ccs.utils.annotation.OperLog)", returning = "keys")
    public void saveTblUserOptLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        assert requestAttributes != null;
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        TblUserOptLog log = new TblUserOptLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OperLog optLog = method.getAnnotation(OperLog.class);
            if (optLog != null) {
                log.setOperModule(optLog.operModul()); // 操作模块
                log.setOperType(optLog.operType()); // 操作类型
                log.setOperDesc(optLog.operDesc()); // 操作描述
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            log.setOperMethod(methodName); // 请求方法
            // 请求的参数
            assert request != null;
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);
            // 这里没有参数就检查是不是因为@RequestBody作为请求参数，调用专门的工具类取解析这样的请求参数
            if (StringUtils.equals(params, "{}")) {
                Object[] args = joinPoint.getArgs();
                params = ObjectMapUtil.getParameterValue(args);
            }
            log.setOperRequParam(params); // 请求参数
//            log.setOperRespParam(JSON.toJSONString(keys)); // 返回结果
            log.setOperUserId(JwtUtil.getUserId(request.getHeader(AosContent.AOS_TOKEN))); // 请求用户ID
            log.setOperUserName(JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN))); // 请求用户名称
            log.setOrg(JwtUtil.getOrg(request.getHeader(AosContent.AOS_TOKEN))); // 请求用户机构
            log.setOperIp(IpAddressUtil.getIpAddress(request)); // 请求IP
            log.setOperUri(request.getRequestURI()); // 请求URI
            log.setOperCreateTime(new Date()); // 创建时间
            // 修改为配置文件控制查询范围
            List<String> types = Arrays.asList(logType.split(","));
            if (types.contains(log.getOperType())) {
                tblOptLogService.insertLog(log);
            }
        } catch (Exception e) {
            logger.error("操作日志INSERT异常-->日志信息{}", log.toString());
        }
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }
}
