package com.allinfinance.dev.feign;

public interface Target<T> {
    Class<T> type();

    String name();

    String url();

    String method();


    public static class HardCodedTarget<T> implements Target<T> {
        private final Class<T> type;
        private final String name;
        private final String url;
        private final String method;

        public HardCodedTarget(Class<T> type, String name, String url, String method) {
            this.type = type;
            this.name = name;
            this.url = url;
            this.method = method;
        }

        @Override
        public Class<T> type() {
            return type;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String url() {
            return url;
        }

        @Override
        public String method() {
            return method;
        }
    }
}

