/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.allinfinance.dev.framework.extension.util;

import cn.hutool.core.util.ArrayUtil;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>类型转换工具类</p>
 * <p>调用端时将类描述转换为字符串传输。服务端将字符串转换为具体的类</p>
 * <pre>
 *     保证传递的时候值为可阅读格式，而不是jvm格式（[Lxxx;）：
 *     普通：java.lang.String、java.lang.String[]
 *     基本类型：int、int[]
 *     内部类：com.alipay.example.Inner、com.alipay.example.Inner[]
 *     匿名类：com.alipay.example.Xxx$1、com.alipay.example.Xxx$1[]
 *     本地类：com.alipay.example.Xxx$1Local、com.alipay.example.Xxx$1Local[]
 *     成员类：com.alipay.example.Xxx$Member、com.alipay.example.Xxx$Member[]
 * 同时Class.forName的时候又会解析出Class。
 * </pre>
 * <p>
 *
 * @author <a href=mailto:zhanggeng.zg@antfin.com>GengZhang</a>
 */
public class ClassTypeUtils {


    static final ConcurrentMap<String, WeakHashMap<ClassLoader, Class>> CLASS_CACHE = new ConcurrentHashMap<>();

    static final ConcurrentMap<Class, String> TYPE_STR_CACHE = new ConcurrentHashMap<Class, String>();

    /**
     * 得到类描述缓存
     *
     * @param clazz 类
     * @return 类描述
     */
    public static String getTypeStrCache(Class clazz) {
        return TYPE_STR_CACHE.get(clazz);
    }

    /**
     * 放入类描述缓存
     *
     * @param clazz   类
     * @param typeStr 对象描述
     */
    public static void putTypeStrCache(Class clazz, String typeStr) {
        TYPE_STR_CACHE.put(clazz, typeStr);
    }


    /**
     * 放入Class缓存
     *
     * @param typeStr 对象描述
     * @param clazz   类
     */
    public static void putClassCache(String typeStr, Class clazz) {
        CLASS_CACHE.putIfAbsent(typeStr, new WeakHashMap<ClassLoader, Class>());
        CLASS_CACHE.get(typeStr).put(clazz.getClassLoader(), clazz);
    }

    /**
     * 得到Class缓存
     *
     * @param typeStr 对象描述
     * @return 类
     */
    public static Class getClassCache(String typeStr) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            return null;
        } else {
            Map<ClassLoader, Class> temp = CLASS_CACHE.get(typeStr);
            return temp == null ? null : temp.get(classLoader);
        }
    }

    /**
     * 通用描述转JVM描述
     *
     * @param canonicalName 例如 int[]
     * @return JVM描述 例如 [I;
     */
    public static String canonicalNameToJvmName(String canonicalName) {
        boolean isArray = canonicalName.endsWith("[]");
        if (isArray) {
            String t = ""; // 计数，看上几维数组
            while (isArray) {
                canonicalName = canonicalName.substring(0, canonicalName.length() - 2);
                t += "[";
                isArray = canonicalName.endsWith("[]");
            }
            if ("boolean".equals(canonicalName)) {
                canonicalName = t + "Z";
            } else if ("byte".equals(canonicalName)) {
                canonicalName = t + "B";
            } else if ("char".equals(canonicalName)) {
                canonicalName = t + "C";
            } else if ("double".equals(canonicalName)) {
                canonicalName = t + "D";
            } else if ("float".equals(canonicalName)) {
                canonicalName = t + "F";
            } else if ("int".equals(canonicalName)) {
                canonicalName = t + "I";
            } else if ("long".equals(canonicalName)) {
                canonicalName = t + "J";
            } else if ("short".equals(canonicalName)) {
                canonicalName = t + "S";
            } else {
                canonicalName = t + "L" + canonicalName + ";";
            }
        }
        return canonicalName;
    }

    /**
     * Class[]转String[] <br>
     * 注意，得到的String可能不能直接用于Class.forName，请使用getClasses(String[])反向获取
     *
     * @param types Class[]
     * @return 对象描述
     */
    public static String[] getTypeStrs(Class[] types) {
        return getTypeStrs(types, false);
    }

    /**
     * Class[]转String[] <br>
     * 注意，得到的String可能不能直接用于Class.forName，请使用getClasses(String[])反向获取
     *
     * @param types     Class[]
     * @param javaStyle JDK自带格式，例如 int[], true的话返回 [I; false的话返回int[]
     * @return 对象描述
     */
    public static String[] getTypeStrs(Class[] types, boolean javaStyle) {
        if (ArrayUtil.isEmpty(types)) {
            return new String[0];
        } else {
            String[] strings = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                strings[i] = javaStyle ? types[i].getName() : getTypeStr(types[i]);
            }
            return strings;
        }
    }

    /**
     * Class转String<br>
     * 注意，得到的String可能不能直接用于Class.forName，请使用getClass(String)反向获取
     *
     * @param clazz Class
     * @return 对象
     */
    public static String getTypeStr(Class clazz) {
        String typeStr = getTypeStrCache(clazz);
        if (typeStr == null) {
            if (clazz.isArray()) {
                String name = clazz.getName(); // 原始名字：[Ljava.lang.String;
                typeStr = jvmNameToCanonicalName(name); // java.lang.String[]
            } else {
                typeStr = clazz.getName();
            }
            putTypeStrCache(clazz, typeStr);
        }
        return typeStr;
    }

    /**
     * JVM描述转通用描述
     *
     * @param jvmName 例如 [I;
     * @return 通用描述 例如 int[]
     */
    public static String jvmNameToCanonicalName(String jvmName) {
        boolean isArray = jvmName.charAt(0) == '[';
        if (isArray) {
            String cnName = ""; // 计数，看上几维数组
            int i = 0;
            for (; i < jvmName.length(); i++) {
                if (jvmName.charAt(i) != '[') {
                    break;
                }
                cnName += "[]";
            }
            String componentType = jvmName.substring(i, jvmName.length());
            if ("Z".equals(componentType)) {
                cnName = "boolean" + cnName;
            } else if ("B".equals(componentType)) {
                cnName = "byte" + cnName;
            } else if ("C".equals(componentType)) {
                cnName = "char" + cnName;
            } else if ("D".equals(componentType)) {
                cnName = "double" + cnName;
            } else if ("F".equals(componentType)) {
                cnName = "float" + cnName;
            } else if ("I".equals(componentType)) {
                cnName = "int" + cnName;
            } else if ("J".equals(componentType)) {
                cnName = "long" + cnName;
            } else if ("S".equals(componentType)) {
                cnName = "short" + cnName;
            } else {
                cnName = componentType.substring(1, componentType.length() - 1) + cnName; // 对象的 去掉L
            }
            return cnName;
        }
        return jvmName;
    }
}
