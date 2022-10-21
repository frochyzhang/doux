package com.allinfinance.dev.common.dictionary.desensitized;

import com.allinfinance.dev.common.dictionary.desensitized.processor.DesensitizedFactory;
import com.allinfinance.dev.common.dictionary.logger.LogUtil;
import com.allinfinance.dev.common.util.constant.CommonConstants;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2021/7/3 02:28
 */
@Component
public class DesensitizedHelperImpl implements DesensitizedHelper {
    private static final Logger logger = LoggerFactory.getLogger(DesensitizedHelperImpl.class);
    private static final Map<Class<?>, List<FieldTypePair>> DESENSITIZED_HOLDER_MAP = new ConcurrentHashMap<>();

    @Autowired
    private DesensitizedFactory desensitizedFactory;
    @Value("${com.allinfinance.epcc.desensitized.packageSuffix:}")
    private String defaultPackages;
    private List<String> packages = new ArrayList<>();

    public static Field[] getFieldsRecursively(Class<?> clazz) {
        List<Field> list = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        ClassUtils.getAllSuperclasses(clazz)
                .forEach(aClass -> list.addAll(Arrays.asList(aClass.getDeclaredFields())));
        return list.toArray(new Field[0]);
    }

    @PostConstruct
    private void init() {
        if (StringUtils.isNotBlank(this.defaultPackages)) {
            this.packages = Arrays.asList(this.defaultPackages.split(","));
        }
    }

    /**
     * 脱敏对象
     *
     * @param object 待脱敏对象
     * @return 脱敏后对象
     */
    @Override
    public String toString(Object object) {
        if (object == null) {
            return null;
        } else if (object.getClass().getDeclaredAnnotation(DesensitizedMark.class) == null && !this.defaultPackage(object)) {
            return String.valueOf(object);
        } else {
            StringBuilder stringBuilder = new StringBuilder("{");

            try {
                this.desensitize(object, this.getFieldsByClass(object), stringBuilder);
                return stringBuilder.toString().replace("{,", "{");
            } catch (Exception e) {
                LogUtil.error(e, logger, "脱敏对象失败,toString返回");
                return String.valueOf(object);
            }
        }
    }

    private void desensitize(Object object, List<FieldTypePair> pairList, StringBuilder result) {
        pairList.forEach(pair -> {
            result.append(',').append(pair.getField().getName()).append('=');
            Object fieldValue = null;
            try {
                pair.getField().setAccessible(true);
                fieldValue = pair.getField().get(object);
            } catch (IllegalAccessException e) {
                LogUtil.error(e, logger, "域{}权限设置异常，请检查", pair.getField());
            }
            if (fieldValue != null && pair.getType() != null) {
                if (pair.getType() == DesensitizedType.OBJECT) {
                    if (fieldValue instanceof Collection) {
                        result.append("[");
                        ((Collection<?>) fieldValue).forEach(it -> result.append(this.toString(it)));
                        result.append("]");
                    } else {
                        result.append(this.toString(fieldValue));
                    }
                } else if (!(fieldValue instanceof Collection)) {
                    result.append(this.desensitizedFactory.desensitized(String.valueOf(fieldValue), pair.type));
                } else {
                    result.append("[");
                    ((Collection<?>) fieldValue).forEach(it -> result.append(this.desensitizedFactory.desensitized(String.valueOf(it), pair.type)).append(","));
                    result.append("]");
                }
            } else {
                result.append(fieldValue);
            }
        });
        result.append("}");
    }

    public DesensitizedFactory getDesensitizedFactory() {
        return desensitizedFactory;
    }

    public void setDesensitizedFactory(DesensitizedFactory desensitizedFactory) {
        this.desensitizedFactory = desensitizedFactory;
    }

    private synchronized List<FieldTypePair> getFieldsByClass(Object object) {
        Class<?> clazz = object.getClass();
        if (!DESENSITIZED_HOLDER_MAP.containsKey(clazz)) {
            Field[] fields = getFieldsRecursively(clazz);
            TypeVariable<? extends Class<?>>[] types = clazz.getTypeParameters();
            List<FieldTypePair> fieldTypePairList = new ArrayList<>(fields.length);

            Arrays.stream(fields).forEach(field -> {
                if (!Modifier.isFinal(field.getModifiers())) {
                    Type fc = field.getGenericType();
                    Desensitized annotation;
                    if (this.containType(types, fc)) {
                        annotation = field.getAnnotation(Desensitized.class);
                        if (annotation != null) {
                            fieldTypePairList.add(new FieldTypePair(field, annotation.value()));
                            return;
                        }
                    }
                    field.setAccessible(true);
                    annotation = field.getAnnotation(Desensitized.class);
                    if (annotation != null) {
                        fieldTypePairList.add(new FieldTypePair(field, annotation.value()));
                    } else if (this.isObject(field)) {
                        fieldTypePairList.add(new FieldTypePair(field, DesensitizedType.OBJECT));
                    } else {
                        fieldTypePairList.add(new FieldTypePair(field, null));
                    }
                }
            });

            DESENSITIZED_HOLDER_MAP.putIfAbsent(clazz, fieldTypePairList);
        }
        return DESENSITIZED_HOLDER_MAP.get(clazz);
    }

    private boolean isObject(Field field) {
        return !field.getType().isPrimitive() && !StringUtils.equals(CommonConstants.STRING_CLASS_NAME, field.getType().getName());
    }

    private boolean containType(TypeVariable<? extends Class<?>>[] types, Type fc) {
        return Arrays.stream(types).allMatch(typeVariable -> typeVariable.equals(fc));
    }

    private boolean defaultPackage(Object object) {
        if (object.getClass().getPackage() == null) {
            return false;
        } else {
            String packageName = object.getClass().getPackage().getName();
            Iterator<String> iterator = this.packages.iterator();
            String entity;
            do {
                if (!iterator.hasNext()) {
                    return false;
                }
                entity = iterator.next();
            } while (!packageName.endsWith(entity));
            return true;
        }
    }

    /**
     * 单字段脱敏
     *
     * @param object   待脱敏字段
     * @param typeEnum 脱敏类型
     * @return 脱敏后字段
     */
    @Override
    public String toString(String object, DesensitizedType typeEnum) {
        if (object == null) {
            return null;
        } else if (typeEnum == null) {
            throw new IllegalArgumentException("脱敏注解不可为空，请检查!");
        } else {
            try {
                return this.desensitizedFactory.desensitized(object, typeEnum);
            } catch (Exception e) {
                LogUtil.error(e, logger, "脱敏单字段失败，toString返回");
                return object;
            }
        }
    }

    private static class FieldTypePair {
        private Field field;
        private DesensitizedType type;

        public FieldTypePair(Field field, DesensitizedType type) {
            this.field = field;
            this.type = type;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public DesensitizedType getType() {
            return type;
        }

        public void setType(DesensitizedType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "FieldTypePair{" +
                    "field=" + field +
                    ", type=" + type +
                    '}';
        }
    }
}
