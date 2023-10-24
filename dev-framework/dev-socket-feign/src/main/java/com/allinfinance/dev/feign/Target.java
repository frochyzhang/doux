package com.allinfinance.dev.feign;

public interface Target<T> {
    Class<T> type();

    String name();

    String url();

    String msgEncode();

    Integer timeout();

    Integer msgLengthSize();

    public static class HardCodedTarget<T> implements Target<T> {
        private final Class<T> type;
        private final String name;
        private final String url;
        private final String msgEncode;
        private final Integer timeout;
        private final Integer msgLengthSize;

        public HardCodedTarget(Class<T> type, String name, String url, String msgEncode, Integer timeout,
            Integer msgLengthSize) {
            this.type = type;
            this.name = name;
            this.url = url;
            this.msgEncode = msgEncode;
            this.timeout = timeout;
            this.msgLengthSize = msgLengthSize;
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
        public String msgEncode() {
            return msgEncode;
        }

        @Override
        public Integer timeout() {
            return timeout;
        }

        @Override
        public Integer msgLengthSize() {
            return msgLengthSize;
        }
    }
}

