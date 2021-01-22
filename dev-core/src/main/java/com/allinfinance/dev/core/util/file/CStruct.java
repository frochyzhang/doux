package com.allinfinance.dev.core.util.file;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.text.MessageFormat;

/**
 * CStruct
 *
 * @author hongmr
 * @date 2017/8/1
 */
public class CStruct<T> {

    private Field[] fields;

    private String charset = "gbk";

    private Class<T> clazz;

    /**
     * 默认使用gbk编码
     *
     * @param clazz
     */
    public CStruct(Class<T> clazz) {
        fields = clazz.getFields();
        this.clazz = clazz;
    }

    public CStruct(Class<T> clazz, String charset) {
        this(clazz);
        this.charset = charset;
    }


    public byte[] toByteArray(T source) {
        assert source != null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (Field field : fields) {
                if ((field.getModifiers() & Modifier.STATIC) > 0) {
                    continue;
                }
                Class<?> type = field.getType();
                Object value = field.get(source);
                if (value == null) {
                    //为null时取默认值
                    if (type.equals(String.class)) {
                        value = "";
                    } else if (Number.class.isAssignableFrom(type)) {
                        value = 0;
                    }
                }

                CChar annoChar = field.getAnnotation(CChar.class);
                CBinaryInt annoInt = field.getAnnotation(CBinaryInt.class);

                if (annoChar != null) {
                    String out;
                    if (value instanceof String) {
                        out = (String) value;
                    } else if (value instanceof Number) {
                        //转成字符串
                        if (StringUtils.isNotBlank(annoChar.formatPattern()))    //优先考虑pattern
                        {
                            out = MessageFormat.format(annoChar.formatPattern(), value);
                        } else {
                            out = MessageFormat.format("{0,number,0}", value);
                        }
                        if (annoChar.zeroPadding()) {
                            //这里由于都是数字，所以长度与字节数一样，可以直接leftPad
                            out = StringUtils.leftPad(out, annoChar.value(), "0");
                        }
                    } else {
                        out = value.toString();
                    }
                    byte[] bytes = out.getBytes(charset);

                    if (bytes.length > annoChar.value()) {
                        baos.write(bytes, 0, annoChar.value());
                    } else {
                        if (annoChar.leftPadding()) {
                            //先输出空格
                            for (int i = 0; i < annoChar.value() - bytes.length; i++) {
                                baos.write(' ');
                            }
                            baos.write(bytes);
                        } else {
                            //先输出内容
                            baos.write(bytes);
                            for (int i = 0; i < annoChar.value() - bytes.length; i++) {
                                baos.write(' ');
                            }
                        }
                    }
                } else if (annoInt != null) {
                    assert value instanceof Number;
                    assert annoInt.length() >= 1 && annoInt.length() <= 8 : "二进制字段长度必须在1到8之间";

                    long l = ((Number) value).longValue();
                    byte[] bytes = new byte[annoInt.length()];
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] = (byte) (l & 0xff);
                    }
                    if (annoInt.bigEndian()) {
                        for (int i = bytes.length - 1; i >= 0; i--) {
                            baos.write(bytes[i]);
                        }
                    } else {
                        baos.write(bytes);
                    }
                } else {
                    assert false : field.getName() + " 必须指定字段类型注释";
                }
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public T parseByteArray(byte[] data) {
        try {
            T obj = clazz.newInstance();

            ByteBuffer buffer = ByteBuffer.wrap(data);

            for (Field field : fields) {
                if ((field.getModifiers() & Modifier.STATIC) > 0) {
                    continue;
                }
                Class<?> type = field.getType();
                CChar annoChar = field.getAnnotation(CChar.class);
                CBinaryInt annoInt = field.getAnnotation(CBinaryInt.class);

                if (annoChar != null) {
                    int len = annoChar.value();
                    byte[] bytes = new byte[len];
                    buffer.get(bytes);
                    String value = new String(bytes, charset);
                    if (type.equals(String.class)) {
                        if (annoChar.autoTrim()) {
                            value = value.trim();
                        }
                        field.set(obj, value);
                    } else if (type.equals(Integer.class)) {
                        field.set(obj, Integer.valueOf(value));
                    } else if (type.equals(Long.class)) {
                        field.set(obj, Long.valueOf(value));
                    } else {
                        throw new IllegalArgumentException("不支持的字段类型:" + type);
                    }
                } else if (annoInt != null) {
                    int value = buffer.getInt();
                    field.set(obj, value);
                } else {
                    assert false : field.getName() + " 必须指定字段类型注释";
                }
            }
            return obj;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public T parseByteArrayWater(byte[] data) {
        try {
            T obj = clazz.newInstance();

            ByteBuffer buffer = ByteBuffer.wrap(data);

            for (Field field : fields) {
                if ((field.getModifiers() & Modifier.STATIC) > 0) {
                    continue;
                }
                Class<?> type = field.getType();

                CChar annoChar = field.getAnnotation(CChar.class);
                CBinaryInt annoInt = field.getAnnotation(CBinaryInt.class);

                if (annoChar != null) {
                    int len = annoChar.value();
                    byte[] bytes = new byte[len + 1];
                    buffer.get(bytes);
                    byte[] resBytes = new byte[len];
                    System.arraycopy(bytes, 0, resBytes, 0, len);
                    String value = new String(resBytes, charset);

                    if (type.equals(String.class)) {
                        if (annoChar.autoTrim()) {
                            value = value.trim();
                        }
                        //字符串直接赋值
                        field.set(obj, value);
                    } else if (type.equals(Integer.class)) {
                        field.set(obj, Integer.valueOf(value));
                    } else if (type.equals(Long.class)) {
                        field.set(obj, Long.valueOf(value));
                    } else {
                        throw new IllegalArgumentException("不支持的字段类型:" + type);
                    }
                } else if (annoInt != null) {
                    int value = buffer.getInt();
                    field.set(obj, value);
                } else {
                    assert false : field.getName() + " 必须指定字段类型注释";
                }
            }
            return obj;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}