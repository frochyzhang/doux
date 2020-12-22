package com.allinfinance.dev.core.util.file;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static boolean zipCompress(String filePath,String zipFilePath) {
        FileInputStream in = null;
        ZipOutputStream zipOut = null;
        try {
            File inputFile = new File(filePath);
            if(!inputFile.exists()){
                logger.error("待压缩文件不存在:{}",filePath);
                return false;
            }
            FileOutputStream fileOut = new FileOutputStream(zipFilePath);
            CheckedOutputStream checkOut = new CheckedOutputStream(fileOut, new CRC32());
            zipOut = new ZipOutputStream(checkOut);
            in = new FileInputStream(filePath);
            int fileindex = filePath.lastIndexOf(File.separator);
            String ZipEn = filePath.substring(fileindex + 1);
            // 建立压缩实体
            zipOut.putNextEntry(new ZipEntry(ZipEn));
            int ch;
            // 当压缩过程未结束时继续读写
            while ((ch = in.read()) != -1) {
                zipOut.write(ch);
            }
            return true;
        }catch (Exception e){
            logger.error("压缩文件出现异常:{}",filePath);
            e.printStackTrace();
            return false;
        }finally{
            try {
                if (null != in)
                    in.close();
                if (null != zipOut)
                    zipOut.close();
            } catch (IOException e) {
                logger.error("关闭文件IO流出现异常!");
                e.printStackTrace();
            }
        }
    }

    public static String sha256File(String filePath){
        String sha256sum = null;
        FileInputStream fileInputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists())
                logger.error("待计算sha-256文件不存在:{}", filePath);
            else {
                fileInputStream = new FileInputStream(file);
                sha256sum = DigestUtils.sha256Hex(fileInputStream);
            }
        }catch (Exception e){
            logger.error("计算文件sha-256出现异常:{}",filePath);
            e.printStackTrace();
        }finally{
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
            }catch (IOException e){
                logger.error("关闭文件IO流出现异常!");
                e.printStackTrace();
            }
        }
        return sha256sum;
    }

}
