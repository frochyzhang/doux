package com.allinfinance.dev.core.util.convert.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * MessageConvert
 *
 * @author jiangjf
 * @date 16/8/30
 */
public class MessageConvert {

    @SuppressWarnings("unchecked")
    public static <T> T beanFromXml(String xml, Class<T> tClass) throws Exception {
        JAXBContext context = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xml));
    }

    public static String beanToXml(Object object) throws Exception {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(object, writer);
        return writer.toString();
    }

    public static <T> T beanFromJson(String json, Class<T> tClass) {
        return JSON.parseObject(json, tClass);
    }

    public static String beanToJson(Object object) {
        return JSON.toJSONString(object);
    }

    public static String getJsonValueByKey(String json, String key) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(key)) {
            return null;
        }
        return JSONObject.parseObject(json).getString(key);
    }
}
