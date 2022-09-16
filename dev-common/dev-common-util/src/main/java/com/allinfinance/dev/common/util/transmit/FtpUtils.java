package com.allinfinance.dev.common.util.transmit;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author qipeng
 * @date 2022/9/13 11:38
 * @desc ftp上传下载工具类
 */
public class FtpUtils {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtils.class);

    /**
     * FTP上传
     *
     * @param remoteIp    服务器hostname
     * @param remotePort  服务器端口
     * @param username    用户名
     * @param password    密码
     * @param ftpPath     服务器路径
     * @param ftpFileName 服务器文件名
     * @param input       输入流
     * @return 成功返回true，否则返回false
     * @throws Exception
     */
    public static boolean uploadFile(String remoteIp, int remotePort, String username,
                                     String password, String ftpPath, String ftpFileName,
                                     InputStream input) throws Exception {
        FTPClient ftp = new FTPClient();
        try {
            int replyCode;
            ftp.connect(remoteIp, remotePort);
            ftp.login(username, password);
            ;
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

            // 检查连接状态
            replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftp.disconnect();
                logger.error("FTP连接异常");
                return false;
            }
            boolean isDirExists = ftp.changeWorkingDirectory(ftpPath);
            if (!isDirExists) {
                logger.error("目录不存在，请检查配置是否正确:" + ftpPath);
                return false;
            }
            // 上传文件
            ftp.enterLocalPassiveMode();
            boolean flag = ftp.storeFile(ftpFileName, input);
            if (!flag) {
                logger.error("FTP上传失败：" + ftpFileName);
                return false;
            } else {
                logger.info("FTP上传成功：" + ftpFileName);
            }
            ftp.logout();
        } catch (IOException e) {
            logger.error("FTP上传出现IO异常", e);
            return false;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error("FTP上传出现IO异常", ioe);
                }
            }
        }
        return true;
    }

    /**
     * FTP下载
     *
     * @param host        服务器hostname
     * @param port        服务器端口
     * @param username    用户名
     * @param password    密码
     * @param ftpPath     服务器路径
     * @param ftpFileName 服务器文件名
     * @param output      输出流
     * @return
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public static boolean downFile(String host, int port, String username,
                                   String password, String ftpPath, String ftpFileName,
                                   OutputStream output) throws Exception {
        FTPClient ftp = new FTPClient();
        try {
            int replyCode;
            ftp.connect(host, port);
            ftp.login(username, password);
            ftp.setFileType(ftp.BINARY_FILE_TYPE);
            // 检查连接状态
            replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftp.disconnect();
                logger.error("FTP连接异常");
                return false;
            }
            boolean isDirExists = ftp.changeWorkingDirectory(ftpPath);
            if (!isDirExists) {
                logger.error("目录不存在，请检查配置是否正确:" + ftpPath);
                return false;
            }
            // 下载文件
            ftp.enterLocalPassiveMode();

            FTPFile[] files = ftp.listFiles(ftpFileName);
            if (files == null || files.length != 1) {
                logger.error("远程文件不存在或不唯一:" + ftpFileName);
                return false;
            }

            boolean flag = ftp.retrieveFile(ftpFileName, output);
            if (!flag) {
                logger.error("FTP下载失败：" + ftpFileName);
                return false;
            } else {
                logger.info("FTP下载成功：" + ftpFileName);
            }
            ftp.logout();
        } catch (IOException e) {
            logger.error("FTP下载出现IO异常", e);
            return false;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error("FTP下载出现IO异常", ioe);
                }
            }
        }
        return true;
    }

    /**
     * FTP上传
     *
     * @param host              服务器hostname
     * @param port              服务器端口
     * @param username          用户名
     * @param password          密码
     * @param ftpPath           服务器路径
     * @param ftpFileName       服务器文件名
     * @param localFileFullName 本地文件名
     * @return 成功返回true，否则返回false
     * @throws Exception
     */
    public static boolean upload(String host, int port, String username,
                                 String password, String ftpPath, String ftpFileName,
                                 String localFileFullName) throws Exception {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(localFileFullName);
            boolean flag = uploadFile(host, port, username, password, ftpPath, ftpFileName, fileInputStream);
            if (flag == false) {
                return false;
            }
        } catch (IOException e) {
            logger.error("FTP上传出现IO异常", e);
            return false;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error("FTP上传出现IO异常", e);
                }
            }
        }
        return true;
    }

    /**
     * FTP下载
     *
     * @param host              服务器hostname
     * @param port              服务器端口
     * @param username          用户名
     * @param password          密码
     * @param ftpPath           服务器路径
     * @param ftpFileName       服务器文件名
     * @param localFileFullName 本地文件名
     * @return
     * @throws Exception
     */
    public static boolean download(String host, int port, String username,
                                   String password, String ftpPath, String ftpFileName,
                                   String localFileFullName) throws Exception {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(localFileFullName);
            boolean flag = downFile(host, port, username, password, ftpPath, ftpFileName, fileOutputStream);
            if (flag == false) {
                return false;
            }
        } catch (IOException e) {
            logger.error("FTP下载出现IO异常", e);
            return false;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error("FTP下载出现IO异常", e);
                }
            }
        }
        return true;
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
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(remoteIp, remotePort);
            ftpClient.login(username, password);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.info("连接ftp服务器失败，用户名或密码错误!");
                ftpClient.disconnect();
                return null;
            } else {
                logger.info("FTP连接成功!");
            }
        } catch (IOException e) {
            logger.info("网络IO异常", e);
            return null;
        }
        return ftpClient;
    }

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
    public static boolean download2(String remoteIp, int remotePort, String username, String password, String remotePath,
                                   String fileName, String localPath) {
        FTPClient ftpClient = getFtpClient(remoteIp, remotePort, username, password);
        if (ObjectUtils.isEmpty(ftpClient)) {
            logger.error("ftp连接失败！");
            return false;
        }

        return true;
    }
}
