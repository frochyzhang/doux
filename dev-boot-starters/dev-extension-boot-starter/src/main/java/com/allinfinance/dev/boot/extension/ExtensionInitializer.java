package com.allinfinance.dev.boot.extension;

import cn.hutool.core.io.FileUtil;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoader;
import com.allinfinance.dev.framework.extension.loader.ExtensionLoaderFactory;
import com.allinfinance.dev.framework.extension.util.ClassLoaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author <a href="mailto:frochyzhang@gmail.com">frochyZhang</a>
 * @date 2022/7/11 15:26
 */
@Configuration
public class ExtensionInitializer implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionInitializer.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        ClassLoader classLoader = ClassLoaderUtils.getClassLoader(getClass());
        Enumeration<URL> urls = classLoader.getResources("META-INF/services/allinfinance/");
        // 可能存在多个文件。
        if (urls != null) {
            while (urls.hasMoreElements()) {
                // 读取一个文件
                URL url = urls.nextElement();
                Arrays.stream(Objects.requireNonNull(new File(url.toURI()).listFiles()))
                        .forEach(extFile -> {
                            String extensible = extFile.getName();
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Loading extension of extensible {} from classloader: {} and file: {}",
                                        extensible, classLoader, url);
                            }
                            try {
                                ExtensionLoader<?> loader = ExtensionLoaderFactory.getExtensionLoader(Class.forName(extensible));
                                FileUtil.readLines(extFile, StandardCharsets.UTF_8).forEach(str -> {
                                    // 为每个扩展添加缓存
                                    loader.getExtension(str.split("=")[0]);
                                });
                            } catch (ClassNotFoundException e) {
                                LOGGER.error("Class Not Found!", e);
                            }
                        });
            }
        }
    }
}
