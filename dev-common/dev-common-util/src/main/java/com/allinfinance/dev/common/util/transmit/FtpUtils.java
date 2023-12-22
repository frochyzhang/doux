package com.allinfinance.dev.common.util.transmit;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author qipeng
 * @date 2022/9/13 11:38
 * @desc ftp上传下载工具类
 */
public class FtpUtils {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtils.class);

    /**
     * ftp下载文件
     *
     * @param remoteIp   远程ip
     * @param remotePort 远程端口
     * @param username   用户名
     * @param password   密码
     * @param remotePath 远程路径
     * @param localPath  本地路径
     * @param fileName   文件名
     * @return 下载成功返回true
     */
    public static boolean download(String remoteIp, int remotePort, String username, String password, String remotePath,
                                   String fileName, String localPath) throws IOException {
        FTPClient ftpClient = getFtpClient(remoteIp, remotePort, username, password);
        if (ObjectUtils.isEmpty(ftpClient)) {
            logger.error("ftp连接失败！");
            return false;
        }

        boolean isDirExists = ftpClient.changeWorkingDirectory(remotePath);
        if (!isDirExists) {
            logger.warn("远程目录不存在，请检查配置是否正确: {}", remotePath);
            return false;
        }

        ftpClient.enterLocalPassiveMode();

        FTPFile[] files = ftpClient.listFiles(fileName);
        if (files == null || files.length != 1) {
            logger.warn("远程文件不存在或不唯一: " + fileName);
            return false;
        }

        try {
            String previousSize = ftpClient.getSize(fileName);
            TimeUnit.MILLISECONDS.sleep(100);
            String latestSize = ftpClient.getSize(fileName);
            if (!previousSize.equals(latestSize)) {
                logger.error("文件大小发生变化，停止下载");
                return false;
            }
        } catch (InterruptedException e) {
            logger.error("判断文件状态异常", e);
            return false;
        }

        String localAbsoluteFile = StringUtils.endsWith(localPath, "/") ? localPath + fileName : localPath + "/" + fileName;
        try (FileOutputStream fileOutputStream = new FileOutputStream(localAbsoluteFile)) {
            boolean flag = ftpClient.retrieveFile(fileName, fileOutputStream);
            if (!flag) {
                logger.warn("FTP下载失败，本地全路径：{}", localAbsoluteFile);
                return false;
            } else {
                logger.info("FTP下载成功：{}", fileName);
                return true;
            }
        } catch (IOException e) {
            logger.error("文件下载异常, 本地全路径：{}", localAbsoluteFile, e);
            return false;
        } finally {
            ftpClient.logout();
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }
    }

    /**
     * ftp上传文件
     *
     * @param remoteIp   远程ip
     * @param remotePort 远程端口
     * @param username   用户名
     * @param password   密码
     * @param remotePath 远程路径
     * @param localPath  本地路径
     * @param fileName   文件名
     * @return 上传成功返回true
     */
    public static boolean upload(String remoteIp, int remotePort, String username, String password, String remotePath,
                                 String fileName, String localPath) throws IOException {
        return upload(remoteIp, remotePort, username, password, remotePath, fileName, localPath, false);
    }

    /**
     * ftp上传文件
     *
     * @param remoteIp   远程ip
     * @param remotePort 远程端口
     * @param username   用户名
     * @param password   密码
     * @param remotePath 远程路径
     * @param localPath  本地路径
     * @param fileName   文件名
     * @param createRemotePath 是否创建远程目录
     * @return 上传成功返回true
     */
    public static boolean upload(String remoteIp, int remotePort, String username, String password, String remotePath,
                                 String fileName, String localPath, boolean createRemotePath) throws IOException {
        FTPClient ftpClient = getFtpClient(remoteIp, remotePort, username, password);
        if (ObjectUtils.isEmpty(ftpClient)) {
            logger.error("ftp连接失败！");
            return false;
        }

        boolean isDirExists = ftpClient.changeWorkingDirectory(remotePath);
        if (!isDirExists) {
            if (createRemotePath) {
                logger.info("远程目录不存在，创建远程目录：{}", remotePath);
                ftpClient.makeDirectory(remotePath);
                if (!ftpClient.changeWorkingDirectory(remotePath)) {
                    logger.warn("远程目录创建失败");
                    return false;
                }
            } else {
                logger.warn("远程目录不存在，请检查配置是否正确: {}", remotePath);
                return false;
            }
        }

        ftpClient.enterLocalPassiveMode();

        String localAbsoluteFile = StringUtils.endsWith(localPath, "/") ? localPath + fileName : localPath + "/" + fileName;
        try (FileInputStream fileInputStream = new FileInputStream(localAbsoluteFile)) {
            boolean flag = ftpClient.storeFile(fileName, fileInputStream);
            if (!flag) {
                logger.warn("FTP上传失败，本地全路径：{}", localAbsoluteFile);
                return false;
            } else {
                logger.info("FTP上传成功：{}", fileName);
                return true;
            }
        } catch (IOException e) {
            logger.error("文件下载异常, 本地全路径：{}", localAbsoluteFile, e);
            return false;
        } finally {
            ftpClient.logout();
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }
    }

    /**
     * 获取ftpClient
     *
     * @param remoteIp   远程ip
     * @param remotePort 远程端口
     * @param username   用户名
     * @param password   密码
     * @return FTPClient
     */
    private static FTPClient getFtpClient(String remoteIp, int remotePort, String username, String password) {
        logger.info("开始获取ftp客户端连接...");
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(remoteIp, remotePort);
            ftpClient.login(username, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.warn("连接ftp服务器失败，请检查用户密码，username：{}，password{}", username, password);
                ftpClient.disconnect();
                throw new RuntimeException("连接ftp服务器失败，请检查用户密码");
            }
        } catch (IOException e) {
            logger.error("网络IO异常，请检查远程配置，remoteIp:{}，remotePort：{}", remoteIp, remotePort, e);
            throw new RuntimeException("网络IO异常，请检查远程配置");
        }
        logger.info("FTP连接成功!");
        return ftpClient;
    }
}
