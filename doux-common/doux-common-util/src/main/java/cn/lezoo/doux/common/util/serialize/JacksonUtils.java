package cn.lezoo.doux.common.util.serialize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanghf
 * @date 2024/1/9 15:08
 */
public class JacksonUtils {
    private static final XmlMapper XML_MAPPER;
    private static final JsonMapper JSON_MAPPER;

    private static final Map<Type, JavaType> javaTypeMap = new ConcurrentHashMap<>();

    static {
        XML_MAPPER = (XmlMapper) XmlMapper.builder()
                .enable(MapperFeature.USE_STD_BEAN_NAMING)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build()
                .registerModule(new KotlinModule.Builder().build())
                .registerModule(new JavaTimeModule());
        JSON_MAPPER = (JsonMapper) JsonMapper.builder()
                .enable(MapperFeature.USE_STD_BEAN_NAMING)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build()
                .registerModule(new KotlinModule.Builder().build())
                .registerModule(new JavaTimeModule());
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

    public static <T> T fromXml(String xml, Type type) {
        try {
            JavaType javaType = javaTypeMap.computeIfAbsent(type, t -> XML_MAPPER.getTypeFactory().constructType(t));
            return XML_MAPPER.readValue(xml, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("XML处理异常", e);
        }
    }

    public static String toXml(Object object) {
        try {
            return XML_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("XML处理异常", e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return JSON_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON处理异常", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        try {
            return JSON_MAPPER.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON处理异常", e);
        }
    }

    public static <T> T fromJson(String json, JavaType javaType) {
        try {
            return JSON_MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON处理异常", e);
        }
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            JavaType javaType = javaTypeMap.computeIfAbsent(type, t -> JSON_MAPPER.getTypeFactory().constructType(t));
            return JSON_MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON处理异常", e);
        }
    }

    public static String toJson(Object object) {
        try {
            return JSON_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON处理异常", e);
        }
    }
}
