package com.allinfinance.dev.ccs.utils;

import com.allinfinance.dev.ccs.content.RSAKeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * RSA非对称加密工具类
 * Created
 */
@Component
public class RSAUtils {

    /**
     * 加密算法
     */
    public static final String ENCRYPT_ALGORITHM = "RSA";

    /**
     * 密钥长度
     */
    private static final int DEFAULT_KEY_SIZE = 2048;


    private static RSAKeyProperties rsaProperties;

    @Autowired
    @Qualifier(value = "rsaKeyProperties")
    public  void setRsaProperties(RSAKeyProperties rsaProperties) {
        RSAUtils.rsaProperties = rsaProperties;
    }

    /**
     * 从文件中读取公钥
     * @param filename 公钥保存路径
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPublicKey(bytes);
    }

    /**
     * 从文件中获取密钥
     * @param filename
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }

    /**
     * 获取公钥
     * @param bytes 公钥字节形式
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        bytes = Base64.getDecoder().decode(bytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(ENCRYPT_ALGORITHM);
        return factory.generatePublic(spec);
    }


    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @return java.lang.String
     * @author liuqi
     * @date 2021-06-11 15:27:07
     */
    public static String encrypt(String data) throws Exception {
        PublicKey publicKey = rsaProperties.getPublicKey();
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(data.getBytes());
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64Utils.encode(bytes));
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @return java.lang.String
     * @author liuqi
     * @date 2021-06-11 15:27:29
     */
    public static String decrypt(String data) throws Exception {
        PrivateKey privateKey = rsaProperties.getPrivateKey();
        KeyFactory factory = KeyFactory.getInstance(ENCRYPT_ALGORITHM);
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.getDecoder().decode(data);
        // 解密后的内容
        byte[] bytes = cipher.doFinal(dataBytes);
        return new String(bytes, "UTF-8");
    }

    /**
     * 获取密钥
     * @param bytes 密钥字节形式
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PrivateKey getPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        bytes = Base64.getDecoder().decode(bytes);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(ENCRYPT_ALGORITHM);
        return factory.generatePrivate(spec);
    }

    /**
     * 根据密文，生成RSA公钥和密钥，并写入文件
     * @param publicKeyFilename     公钥文件路径
     * @param privateKeyFilename    私钥文件路径
     * @param secret                生成密钥的密文
     * @param keySize               指定密钥长度，如果比默认小则选择默认长度2048
     * @throws Exception
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret, int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ENCRYPT_ALGORITHM);
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(Math.max(keySize, DEFAULT_KEY_SIZE), secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        // 获取公钥并写出
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        publicKeyBytes = Base64.getEncoder().encode(publicKeyBytes);
        writeFile(publicKeyFilename, publicKeyBytes);

        // 获取私钥并写出
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        privateKeyBytes = Base64.getEncoder().encode(privateKeyBytes);
        writeFile(privateKeyFilename, privateKeyBytes);
    }

    private static byte[] readFile(String filename) throws IOException {
        return Files.readAllBytes(new File(filename).toPath());
    }

    private static void writeFile(String filename, byte[] bytes) throws IOException {
        File file = new File(filename);
        File fileParent = file.getParentFile();
        if (!file.exists()) {
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            file.createNewFile();
        }
        Files.write(file.toPath(), bytes);
    }

    public static void main(String[] args) {
        String mm="UHXbGbHo82xFzBp+EN+vcFl+x7371VvE9fbdpGh+e4xu1dFfJV7vLu4pXn64NfkY9oR56RC/jtUSqoYnUvK8F6ua052282pAaFMGjtzFoQUNKyV/W3OwtleaUNno5SciuhJR1wJEm6gStdT9qEgGiaf5wxjnO4gN9RZ3SvYJHpPE6fvtFWrXdWCJf2OPTVeLEoFbvITVJRVDlenedl9Icb43QEf5VkeeVeK/AeefsmUqFx+38G6zLO2UqswhvFcT23op+1Rj4htvHa0bQdWyVab+5v7rulVWt1ZW54YnsmagoutlOT3yUF1uRGnjrmWpNPJHZvCFeodykqcfpCIcnw==";
    }
}
