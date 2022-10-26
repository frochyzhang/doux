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
    public static final ConcurrentHashMap<String, XStream> xStream = new ConcurrentHashMap<>();
    public static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static String ALIAS = "SERVICE";

    private static XStream getXStream(Class<?> obj) {
        String key = obj.getName();
        //防止多线程并发下重复创建对象
        if (!xStream.containsKey(key)) {
            synchronized (xStream) {
                if (!xStream.containsKey(key)) {
                    XStream xs = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
                    xs.ignoreUnknownElements();
                    xs.autodetectAnnotations(true);
                    xs.alias(ALIAS, obj);
                    xStream.put(key, xs);
                }
            }
        }
        return xStream.get(key);
    }

    public static String beanToXml(Object object) {
        XStream xs = getXStream(object.getClass());
        return XML_HEAD + "\n" + xs.toXML(object);
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
    public static void setALIAS(String alias) {
        ALIAS = alias;
    }

    /**
     * 对报文头设置
     *
     * @param xmlHead 报文头，默认值为<?xml version="1.0" encoding="UTF-8"?>
     */
    public static void setXmlHead(String xmlHead) {
        XML_HEAD = xmlHead;
    }
}
