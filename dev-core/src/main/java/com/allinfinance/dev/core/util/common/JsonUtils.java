package com.allinfinance.dev.core.util.common;

import com.alibaba.fastjson.JSONObject;
import com.allinfinance.dev.core.constant.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * JsonUtils
 *
 * @author jiangjf
 * @date 2017/1/12
 */
public class JsonUtils {
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 将对象的属性按升序排列返回
     *
     * @param t
     * @param exculdeKeys 不参与排序的属性值
     * @return
     * @throws Exception
     */
    public static <T> String sortBeanValueString(T t, String... exculdeKeys) throws Exception {

        if (t == null) {
            logger.error("待处理的json源对象为空！");
            return "";
        }
        String jsonString = JSONObject.toJSONString(t);
        JSONObject jsonObj = JSONObject.parseObject(jsonString);
        String[] keys = jsonObj.keySet().toArray(new String[]{});
        Arrays.sort(keys);
        StringBuilder strBuilder = new StringBuilder();
        for (String key : keys) {
            String value = jsonObj.getString(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            if (!isContainStr(key, exculdeKeys)) {
                strBuilder.append(value);
            }
        }
        return strBuilder.toString();
    }


    /**
     * 将对象的属性按升序排列返回,key=value
     *
     * @param t
     * @param exculdeKeys 不参与排序的属性值
     * @return
     * @throws Exception
     */
    public static <T> String sortBeanValueStringWithKey(T t, String joinFix, String... exculdeKeys) throws Exception {

        if (t == null) {
            logger.error("待处理的json源对象为空！");
            return "";
        }
        String jsonString = JSONObject.toJSONString(t);
        JSONObject jsonObj = JSONObject.parseObject(jsonString);
        String[] keys = jsonObj.keySet().toArray(new String[]{});
        Arrays.sort(keys);
        StringBuilder strBuilder = new StringBuilder();
        for (String key : keys) {
            String value = jsonObj.getString(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            if (!isContainStr(key, exculdeKeys)) {
                strBuilder.append(key + CommonConstants.EQUAL + value + joinFix);
            }
        }
        return strBuilder.substring(0, strBuilder.length() - 1);
    }


    /**
     * 根据传入的json字符串，按key升序排列返回结果
     *
     * @param json
     * @param exculdeKeys 剔除不参与排序的字段
     * @return
     * @throws Exception
     */
    public static String sortJsonValueString(String json, String... exculdeKeys) throws Exception {

        if (StringUtils.isBlank(json)) {
            logger.error("待处理的json源对象为空！");
            return "";
        }
        JSONObject jsonObj = JSONObject.parseObject(json);
        String[] keys = jsonObj.keySet().toArray(new String[]{});
        Arrays.sort(keys);
        StringBuilder strBuilder = new StringBuilder();
        for (String key : keys) {
            String value = jsonObj.getString(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            if (!isContainStr(key, exculdeKeys)) {
                strBuilder.append(value);
            }
        }
        return strBuilder.toString();
    }

    /**
     * strs中是否包含str
     *
     * @param str
     * @param strs
     * @return
     */
    public static boolean isContainStr(String str, String... strs) {
        if (strs != null && StringUtils.isNotBlank(str)) {
            for (int i = 0; i < strs.length; i++) {
                if (str.trim().equals(strs[i].trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取json字符串中某个字段值
     *
     * @param json
     * @param key
     * @return
     */
    public static String getJsonValueByKey(String json, String key) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(key)) {
            return null;
        }
        return JSONObject.parseObject(json).getString(key);
    }
}
