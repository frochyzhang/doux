package cn.lezoo.doux.common.util.xml;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huanghf
 * @date 2024/6/26 14:51
 */
public class XmlUtils {
    private static final Map<String, Pattern> patternMap = new ConcurrentHashMap<>();

    public static String getValueWithLabel(String xmlString, String label) {
        Pattern pattern = patternMap.get(label);
        if (pattern == null) {
            pattern = Pattern.compile("(<" + label + ">[\\s\\S]+</" + label + ">)");
            patternMap.put(label, pattern);
        }
        Matcher matcher = pattern.matcher(xmlString);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static String getValueWithoutLabel(String xmlString, String label) {
        Pattern pattern = patternMap.get(label);
        if (pattern == null) {
            pattern = Pattern.compile("<" + label + ">([\\s\\S]+)</" + label + ">");
            patternMap.put(label, pattern);
        }
        Matcher matcher = pattern.matcher(xmlString);
        return matcher.find() ? matcher.group(1) : null;
    }
}
