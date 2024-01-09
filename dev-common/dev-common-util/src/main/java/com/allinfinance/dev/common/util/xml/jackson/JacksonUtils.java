package com.allinfinance.dev.common.util.xml.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author huanghf
 * @date 2024/1/9 15:08
 */
public class JacksonUtils {
    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final XmlMapper XML_MAPPER;

    static {
        XML_MAPPER = (XmlMapper) new XmlMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T fromXml(String xml, TypeReference<T> typeReference) {
        try {
            return XML_MAPPER.readValue(xml, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("XML处理异常", e);
        }
    }

    public static <T> T fromXml(String xml, Class<T> tClass) {
        try {
            return XML_MAPPER.readValue(xml, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("XML处理异常", e);
        }
    }

    public static <T> T fromXml(String xml, JavaType javaType) {
        try {
            return XML_MAPPER.readValue(xml, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("XML处理异常", e);
        }
    }

    public static String toXml(Object object) {
        try {
            return XML_HEAD + "\n" + XML_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("XML处理异常", e);
        }
    }
}
