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
package cn.lezoo.doux.framework.extension.loader;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Factory of ExtensionLoader
 *
 * @author <a href=mailto:zhanggeng.zg@antfin.com>GengZhang</a>
 */
public class ExtensionLoaderFactory {
    private ExtensionLoaderFactory() {
    }

    /**
     * All extension loader {Class : ExtensionLoader}
     */
    private static final ConcurrentMap<Class, ExtensionLoader> LOADER_MAP = new ConcurrentHashMap<>();

    /**
     * Get extension loader by extensible class with listener
     *
     * This method is deprecated, use com.alipay.sofa.rpc.ext.ExtensionLoaderFactory#getExtensionLoader(java.lang.Class) instead.
     * Use com.alipay.sofa.rpc.ext.ExtensionLoader#addListener(com.alipay.sofa.rpc.ext.ExtensionLoaderListener) to add listener.
     *
     * @deprecated
     * @param clazz    Extensible class
     * @param listener Listener of ExtensionLoader
     * @param <T>      Class
     * @return ExtensionLoader of this class
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz, ExtensionLoaderListener<T> listener) {
        ExtensionLoader<T> loader = LOADER_MAP.get(clazz);
        if (loader == null) {
            synchronized (ExtensionLoaderFactory.class) {
                loader = LOADER_MAP.get(clazz);
                if (loader == null) {
                    loader = new ExtensionLoader<T>(clazz, listener);
                    LOADER_MAP.put(clazz, loader);
                }
            }
        }
        return loader;
    }

    /**
     * Get extension loader by extensible class without listener
     *
     * @param clazz Extensible class
     * @param <T>   Class
     * @return ExtensionLoader of this class
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
        return getExtensionLoader(clazz, null);
    }

    /**
     * Get extension by extensible class and alias
     *
     * @param clazz Extensible class
     * @param alias 别名
     * @return 扩展实例（已判断是否单例）
     */
    public static <T> T getExtension(Class<T> clazz, String alias) {
        ExtensionLoader<T> extensionLoader = getExtensionLoader(clazz);
        return extensionLoader.getExtension(alias);
    }

    /**
     * Get extension by extensible class, alias, argTypes and args
     *
     * @param clazz    Extensible class
     * @param alias    别名
     * @param argTypes 扩展初始化需要的参数类型
     * @param args     扩展初始化需要的参数
     * @return 扩展实例（已判断是否单例）
     */
    public static <T> T getExtension(Class<T> clazz, String alias, Class[] argTypes, Object[] args) {
        ExtensionLoader<T> extensionLoader = getExtensionLoader(clazz);
        return extensionLoader.getExtension(alias, argTypes, args);
    }
}
