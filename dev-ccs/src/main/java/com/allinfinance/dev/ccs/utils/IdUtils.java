package com.allinfinance.dev.ccs.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IdUtils {
    public static String getId() {
        return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + String.format("%06d", new Random().nextInt(1000000));
    }
}
