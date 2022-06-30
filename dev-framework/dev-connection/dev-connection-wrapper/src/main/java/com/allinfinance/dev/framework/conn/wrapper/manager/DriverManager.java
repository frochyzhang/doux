/*
 * Copyright [2022/6/29] [<a href="mailto:frochyzhang@gmail.com>frochyZhang</a>]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.allinfinance.dev.framework.conn.wrapper.manager;


import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * @author <a href="mailto:frochyzhang@gmail.com>frochyZhang</a>
 * @date 2022/6/29 14:26
 */
public class DriverManager {
    public static Connection getConnection(Properties properties) {
        Connection conn = null;
        Iterator<Connection> connectionIterator = ServiceLoader.load(Connection.class).iterator();
        if (connectionIterator.hasNext()) {
            conn = connectionIterator.next();
            conn.connect(properties);
        }
        return conn;
    }
}
