package com.allinfinance.dev.white.list.diversion.http.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huanghf
 * @date 2024/3/21 17:12
 */
public class RequestMsgUtils {
    public static String getXmlLabelValue(String label, String request) {
        String sHeader = "<" + label + ">";
        String sTail = "</" + label + ">";
        if (!request.contains(label)) {
            return "";
        } else {
            int beginIndex = request.indexOf(sHeader) + sHeader.length();
            int endIndex = request.indexOf(sTail);
            return request.substring(beginIndex, endIndex);
        }
    }

    public static String getJsonNodeValue(String label, String request) {
        Pattern pattern = Pattern.compile("\"" + label + "\":\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(request);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    public static Map<String, String> getHeader(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    public static Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parameters.put(parameterName, request.getParameter(parameterName));
        }
        return parameters;
    }
}
