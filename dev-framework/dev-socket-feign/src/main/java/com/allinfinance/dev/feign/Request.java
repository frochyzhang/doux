/*
 * Copyright 2012-2023 The Feign Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.allinfinance.dev.feign;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * An immutable request to an http server.
 */
public final class Request implements Serializable {
    private final String method;

    private final String url;

    private transient Charset encoding;

    private Object data;


    public Request(String method, String url, Charset encoding, Object data) {
        this.method = method;
        this.url = url;
        this.encoding = encoding;
        this.data = data;
    }

    public static Request create(String method, String url, Charset encoding, Object data) {
        return new Request(method, url, encoding, data);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Charset getEncoding() {
        return encoding;
    }

    public Object getData() {
        return data;
    }

    public static class Options {

        private final long connectTimeout;
        private final TimeUnit connectTimeoutUnit;
        private final long readTimeout;
        private final TimeUnit readTimeoutUnit;
        private final boolean followRedirects;

        /**
         * Creates a new Options Instance.
         *
         * @param connectTimeout     value.
         * @param connectTimeoutUnit with the TimeUnit for the timeout value.
         * @param readTimeout        value.
         * @param readTimeoutUnit    with the TimeUnit for the timeout value.
         * @param followRedirects    if the request should follow 3xx redirections.
         */
        public Options(long connectTimeout, TimeUnit connectTimeoutUnit,
            long readTimeout, TimeUnit readTimeoutUnit,
            boolean followRedirects) {
            super();
            this.connectTimeout = connectTimeout;
            this.connectTimeoutUnit = connectTimeoutUnit;
            this.readTimeout = readTimeout;
            this.readTimeoutUnit = readTimeoutUnit;
            this.followRedirects = followRedirects;
        }

        /**
         * Creates a new Options Instance.
         *
         * @param connectTimeout  value.
         * @param readTimeout     value.
         * @param followRedirects if the request should follow 3xx redirections.
         */
        public Options(Duration connectTimeout, Duration readTimeout, boolean followRedirects) {
            this(connectTimeout.toMillis(), TimeUnit.MILLISECONDS, readTimeout.toMillis(),
                TimeUnit.MILLISECONDS, followRedirects);
        }

        /**
         * Creates the new Options instance using the following defaults:
         * <ul>
         * <li>Connect Timeout: 10 seconds</li>
         * <li>Read Timeout: 60 seconds</li>
         * <li>Follow all 3xx redirects</li>
         * </ul>
         */
        public Options() {
            this(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true);
        }

        /**
         * Defaults to 10 seconds. {@code 0} implies no timeout.
         *
         * @see HttpURLConnection#getConnectTimeout()
         */
        public int connectTimeoutMillis() {
            return (int) connectTimeoutUnit.toMillis(connectTimeout);
        }

        /**
         * Defaults to 60 seconds. {@code 0} implies no timeout.
         *
         * @see HttpURLConnection#getReadTimeout()
         */
        public int readTimeoutMillis() {
            return (int) readTimeoutUnit.toMillis(readTimeout);
        }


        /**
         * Defaults to true. {@code false} tells the client to not follow the redirections.
         *
         * @see HttpURLConnection#getFollowRedirects()
         */
        public boolean isFollowRedirects() {
            return followRedirects;
        }

        /**
         * Connect Timeout Value.
         *
         * @return current timeout value.
         */
        public long connectTimeout() {
            return connectTimeout;
        }

        /**
         * TimeUnit for the Connection Timeout value.
         *
         * @return TimeUnit
         */
        public TimeUnit connectTimeoutUnit() {
            return connectTimeoutUnit;
        }

        /**
         * Read Timeout value.
         *
         * @return current read timeout value.
         */
        public long readTimeout() {
            return readTimeout;
        }

        /**
         * TimeUnit for the Read Timeout value.
         *
         * @return TimeUnit
         */
        public TimeUnit readTimeoutUnit() {
            return readTimeoutUnit;
        }

    }
}
