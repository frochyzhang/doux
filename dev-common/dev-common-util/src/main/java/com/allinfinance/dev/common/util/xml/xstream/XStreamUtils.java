package com.allinfinance.dev.common.util.xml.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:liumiao@allinfinance.com">liumiao</a>
 * @date 2022-10-24 14:33
 */
public class XStreamUtils {
    public static final ConcurrentHashMap<String, XStream> X_STREAM = new ConcurrentHashMap<>();
    private static String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static String alias = "SERVICE";

    private static XStream getXStream(Class<?> obj) {
        String key = obj.getName();
        //防止多线程并发下重复创建对象
        if (!X_STREAM.containsKey(key)) {
            synchronized (X_STREAM) {
                if (!X_STREAM.containsKey(key)) {
                    XStream xs = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
                    xs.ignoreUnknownElements();
                    xs.autodetectAnnotations(true);
                    xs.alias(alias, obj);
                    X_STREAM.put(key, xs);
                }
            }
        }
        return X_STREAM.get(key);
    }

    public static String beanToXml(Object object) {
        XStream xs = getXStream(object.getClass());
        return xmlHead + "\n" + xs.toXML(object);
    }

    public static String beanToXml(Object object, String encoding) {
        XStream xs = getXStream(object.getClass());
        return String.format("<?xml version=\"1.0\" encoding=\"%s\"?>", encoding) + "\n" + xs.toXML(object);
    }

    public static String beanToXml(Object object, String version, String encoding) {
        XStream xs = getXStream(object.getClass());
        return String.format("<?xml version=\"%s\" encoding=\"%s\"?>", version, encoding) + "\n" + xs.toXML(object);
    }

    public static <T> T xmlToBean(String xml, Class<T> clazz) {
        XStream xs = getXStream(clazz);
        T o = (T) xs.fromXML(xml);
        return o;
    }

    public static <T> T xmlToBean(String xml, Class<T> clazz, String alias) {
        XStream xs = getXStream(clazz);
        xs.alias(alias, clazz);
        T o = (T) xs.fromXML(xml);
        return o;
    }

    /**
     * 对报文根节点别名设置
     *
     * @param alias 根节点别名，默认值为SERVICE
     */
    public static void setAlias(String alias) {
        XStreamUtils.alias = alias;
    }

    /**
     * 对报文头设置
     *
     * @param xmlHead 报文头，默认值为<?xml version="1.0" encoding="UTF-8"?>
     */
    public static void setXmlHead(String xmlHead) {
        XStreamUtils.xmlHead = xmlHead;
    }
}
