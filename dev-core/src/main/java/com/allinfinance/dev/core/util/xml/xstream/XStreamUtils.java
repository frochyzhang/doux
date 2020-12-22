package com.allinfinance.dev.core.util.xml.xstream;

import com.allinfinance.dev.core.util.xml.XmlConvertValidator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 张勇
 * @description 该工具类主要永远XStream报文与对象的
 * 互转XStream对象已设为静态变量，在之前的版本中出现了
 * 由于频繁创建XStream对象导致的性能问题
 * @date 2020/12/8 13:53
 */
public class XStreamUtils {
    private static final Logger logger = LoggerFactory.getLogger(XStreamUtils.class);

    public static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static String alias = "SERVICE";

    public static XStream xsFriendly;
    public static XStream xs;


    static {
        xsFriendly = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
        xs = new XStream() {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn,
                                                         String fieldName) {
                        if (definedIn == Object.class) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn,
                                fieldName);
                    }
                };
            }
        };
    }

    public static Object xmlToBean(String xml, Class<?> load, boolean omitFlag) {
        if (!omitFlag) {
            xs = xsFriendly;
        }
        xs.autodetectAnnotations(true);
        xs.alias(alias, load);
        Object o = xs.fromXML(xml);
        try {
            XmlConvertValidator.beanToXmlVerify(o, "UTF-8");
        } catch (IllegalArgumentException e) {
            logger.error("xml -> bean, 字段校验异常!");
            throw e;
        }
        return o;
    }

    public static String beanToXml(Object object, String encoding) {
        try {
            XmlConvertValidator.beanToXmlVerify(object, encoding);
        } catch (IllegalArgumentException e) {
            logger.error("bean -> xml, 字段校验异常!");
            throw e;
        }
        xsFriendly.alias(alias, object.getClass());
        xsFriendly.processAnnotations(object.getClass());
        return XML_HEAD + "\n" + xsFriendly.toXML(object);
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
        XML_HEAD = xmlHead;
    }
}
