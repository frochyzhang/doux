package com.allinfinance.dev.hsp.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DigestUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(DigestUtil.class);

    @Autowired
    private DigestUtil digestUtil;

    @Test
    void getDigest() {
        //digestUtil.getDigest(HashAlgorithmEnum.SM3, "")
    }
}