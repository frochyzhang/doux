package com.allinfinance.dev.white.list.diversion.socket.util;

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
}
