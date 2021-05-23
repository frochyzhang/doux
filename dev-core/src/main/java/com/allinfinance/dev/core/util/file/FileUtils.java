package com.allinfinance.dev.core.util.file;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * FileUtils
 *
 * @author hongmr
 * @date 2017/5/19
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static boolean zipCompress(String filePath, String zipFilePath) {
        try (FileOutputStream fileOut = new FileOutputStream(zipFilePath);
             CheckedOutputStream checkOut = new CheckedOutputStream(fileOut, new CRC32());
             FileInputStream in = new FileInputStream(filePath);
             ZipOutputStream zipOut = new ZipOutputStream(checkOut)) {
            File inputFile = new File(filePath);
            if (!inputFile.exists()) {
                logger.error("待压缩文件不存在:{}", filePath);
                return false;
            }
            int fileindex = filePath.lastIndexOf(File.separator);
            String zipEn = filePath.substring(fileindex + 1);
            // 建立压缩实体
            zipOut.putNextEntry(new ZipEntry(zipEn));
            int ch;
            // 当压缩过程未结束时继续读写
            while ((ch = in.read()) != -1) {
                zipOut.write(ch);
            }
            return true;
        } catch (Exception e) {
            logger.error("压缩文件出现异常:{}", filePath);
            e.printStackTrace();
            return false;
        }
    }

    public static String sha256File(String filePath) {
        String sha256sum = null;
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("待计算sha-256文件不存在:{}", filePath);
            return sha256sum;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            sha256sum = DigestUtils.sha256Hex(fileInputStream);
        } catch (Exception e) {
            logger.error("计算文件sha-256出现异常:{}", filePath);
            e.printStackTrace();
        }
        return sha256sum;
    }

}
