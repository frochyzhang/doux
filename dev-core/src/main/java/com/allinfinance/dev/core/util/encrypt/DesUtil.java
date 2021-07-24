package com.allinfinance.dev.core.util.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * DesUtil
 *
 * @author hongmr
 * @date 2017/5/16
 */
public class DesUtil {

    /**
     * 3Des加密非填充
     *
     * @param hexKey
     * @param hexData
     * @return
     * @throws Exception
     */
    public static String encryptDesSede(String hexKey, String hexData) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        String Algorithm = "DESede/ECB/NoPadding";

        byte[] hb = DesUtil.decode(hexKey);
        byte[] k = new byte[24];
        System.arraycopy(hb, 0, k, 0, 16);
        System.arraycopy(hb, 0, k, 16, 8);

        SecretKey desKey = new SecretKeySpec(k, Algorithm);

        Cipher cp = Cipher.getInstance(Algorithm);
        cp.init(Cipher.ENCRYPT_MODE, desKey);
        byte[] bytes = cp.doFinal(DesUtil.decode(hexData));

        return DesUtil.encode(bytes);
    }

    /**
     * 3Des解密非填充
     *
     * @param hexKey
     * @param hexData
     * @return
     * @throws Exception
     */
    public static String decryptDesSede(String hexKey, String hexData) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        String Algorithm = "DESede/ECB/NoPadding";

        byte[] hb = DesUtil.decode(hexKey);
        byte[] k = new byte[24];
        System.arraycopy(hb, 0, k, 0, 16);
        System.arraycopy(hb, 0, k, 16, 8);

        SecretKey desKey = new SecretKeySpec(k, Algorithm);

        Cipher cp = Cipher.getInstance(Algorithm);
        cp.init(Cipher.DECRYPT_MODE, desKey);
        byte[] bytes = cp.doFinal(DesUtil.decode(hexData));

        return DesUtil.encode(bytes);
    }

    /**
     * Creates a clone of the given byte array.
     */
    private static byte[] getClone(byte[] pHexBinary) {
        byte[] result = new byte[pHexBinary.length];
        System.arraycopy(pHexBinary, 0, result, 0, pHexBinary.length);
        return result;
    }

    /**
     * Converts the string into an array of hex bytes.
     */
    private static byte[] decode(String pValue) {
        if ((pValue.length() % 2) != 0) {
            throw new IllegalArgumentException("A HexBinary string must have even length.");
        }
        byte[] result = new byte[pValue.length() / 2];
        int j = 0;
        for (int i = 0; i < pValue.length(); ) {
            byte b;
            char c = pValue.charAt(i++);
            char d = pValue.charAt(i++);
            if (c >= '0' && c <= '9') {
                b = (byte) ((c - '0') << 4);
            } else if (c >= 'A' && c <= 'F') {
                b = (byte) ((c - 'A' + 10) << 4);
            } else if (c >= 'a' && c <= 'f') {
                b = (byte) ((c - 'a' + 10) << 4);
            } else {
                throw new IllegalArgumentException("Invalid hex digit: " + c);
            }
            if (d >= '0' && d <= '9') {
                b += (byte) (d - '0');
            } else if (d >= 'A' && d <= 'F') {
                b += (byte) (d - 'A' + 10);
            } else if (d >= 'a' && d <= 'f') {
                b += (byte) (d - 'a' + 10);
            } else {
                throw new IllegalArgumentException("Invalid hex digit: " + d);
            }
            result[j++] = b;
        }
        return result;
    }

    /**
     * Converts the byte array into a string.
     */
    private static String encode(byte[] pHexBinary) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < pHexBinary.length; i++) {
            byte b = pHexBinary[i];
            byte c = (byte) ((b & 0xf0) >> 4);
            if (c <= 9) {
                result.append((char) ('0' + c));
            } else {
                result.append((char) ('A' + c - 10));
            }
            c = (byte) (b & 0x0f);
            if (c <= 9) {
                result.append((char) ('0' + c));
            } else {
                result.append((char) ('A' + c - 10));
            }
        }
        return result.toString();
    }
}
