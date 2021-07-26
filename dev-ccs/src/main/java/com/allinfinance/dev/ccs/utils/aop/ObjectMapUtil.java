package com.allinfinance.dev.ccs.utils.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allinfinance.dev.ccs.controller.OptLogController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/7/26 10:54
 * @description：处理request请求参数的工具类
 */
public class ObjectMapUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectMapUtil.class);

    public static String getParameterValue(Object objs[]) {
        String jsonObj ="";
        try {
            for (Object arg : objs) {
                // 加上getClass 是因为部分的参数获取不到全类名  另此处获取的的是该controller的全部参数
                // com.allinfinance只取这些参数中 自己定义的参数
                if (arg.getClass().toString().contains("com.allinfinance")) {
                    jsonObj = JSONArray.toJSONString(arg);
                }
            }
        } catch (Exception e) {
            logger.error("操作日志@RequestBody参数解析异常");
            return "{}";
        }

        return jsonObj;
    }
}
