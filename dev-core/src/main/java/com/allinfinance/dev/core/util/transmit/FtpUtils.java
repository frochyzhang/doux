package com.allinfinance.dev.core.util.transmit;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * FtpUtils
 *
 * @author hongmr
 * @date 2017/1/10
 */
public class FtpUtils {
    private static Logger logger = LoggerFactory.getLogger(FtpUtils.class);

    /**
     * FTP上传
     *
     * @param host        服务器hostname
     * @param port        服务器端口
     * @param username    用户名
     * @param password    密码
     * @param ftpPath     服务器路径
     * @param ftpFileName 服务器文件名
     * @param input       输入流
     * @return 成功返回true，否则返回false
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public static boolean uploadFile(String host, int port, String username,
                                     String password, String ftpPath, String ftpFileName,
                                     InputStream input) throws Exception {
        FTPClient ftp = new FTPClient();

        try {
            int replyCode;
            ftp.connect(host, port);
            ftp.login(username, password);
            ;
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
            // 上传文件
            ftp.enterLocalPassiveMode();
            boolean flag = ftp.storeFile(ftpFileName, input);
            if (!flag) {
                logger.error("FTP上传失败：" + ftpFileName);
                return false;
            } else
                logger.info("FTP上传成功：" + ftpFileName);
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
                    return false;
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
            } else
                logger.info("FTP下载成功：" + ftpFileName);
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
                    return false;
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
            if (flag == false)
                return false;
        } catch (IOException e) {
            logger.error("FTP上传出现IO异常", e);
            return false;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error("FTP上传出现IO异常", e);
                    return false;
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
            if (flag == false)
                return false;
        } catch (IOException e) {
            logger.error("FTP下载出现IO异常", e);
            return false;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error("FTP下载出现IO异常", e);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * FTP下载-支持断点续传
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
    @SuppressWarnings("static-access")
    public static boolean downloadFileResume(String host, int port, String username,
                                             String password, String ftpPath, String ftpFileName,
                                             String localFileFullName) throws Exception {
        FTPClient ftp = new FTPClient();
        OutputStream os = null;
        try {
            int replyCode;
            ftp.connect(host, port);
            ftp.login(username, password);
            ftp.setFileType(ftp.BINARY_FILE_TYPE);
            File f = new File(localFileFullName);
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
            long remoteSize = files[0].getSize();
            logger.info("远程文件大小：" + remoteSize);
            if (f.exists()) {
                os = new FileOutputStream(f, true);
                logger.info("本地文件已存在:{},大小:{}", ftpFileName, f.length());
                if (f.length() > remoteSize) {
                    logger.error("本地文件大于远程文件:" + ftpFileName);
                    return false;
                } else if (f.length() == remoteSize) {
                    logger.info("文件已成功下载，无需继续下载:" + ftpFileName);
                    return true;
                }
                ftp.setRestartOffset(f.length());
                logger.info("需要断点下载:" + ftpFileName);
            } else {
                logger.info("本地文件尚未创建:" + ftpFileName);
                os = new FileOutputStream(f);
            }
            logger.info("ftp文件下载开始:" + ftpFileName);
            boolean flag = ftp.retrieveFile(ftpFileName, os);
            if (!flag) {
                logger.error("FTP下载失败:" + ftpFileName);
                return false;
            } else
                logger.info("FTP下载成功：" + ftpFileName);
            ftp.logout();
        } catch (IOException e) {
            logger.error("FTP下载出现IO异常", e);
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("FTP下载出现IO异常", e);
                    return false;
                }
            }
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error("FTP下载出现IO异常", ioe);
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * FTP上传-支持断点续传
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
    @SuppressWarnings("static-access")
    public static boolean uploadFileResume(String host, int port, String username,
                                           String password, String ftpPath, String ftpFileName,
                                           String localFileFullName) throws Exception {
        FTPClient ftp = new FTPClient();
        FileInputStream is = null;
        try {
            int replyCode;
            ftp.connect(host, port);
            ftp.login(username, password);
            ;
            ftp.setFileType(ftp.BINARY_FILE_TYPE);

            // 检查连接状态
            replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftp.disconnect();
                logger.error("FTP连接异常");
                return false;
            }

            //判断源文件目录是否存在
            boolean isDirExists = ftp.changeWorkingDirectory(ftpPath);
            if (!isDirExists) {
                logger.info("目录不存在，创建目录:" + ftpPath);
                if (!ftp.makeDirectory(ftpPath)) {
                    logger.error("创建目录失败:" + ftpPath);
                    return false;
                }
                ftp.changeWorkingDirectory(ftpPath);
            }

            // 上传文件
            ftp.enterLocalPassiveMode();

            File f = new File(localFileFullName);
            long localFileSize = f.length();
            logger.info("本地文件大小:" + localFileSize);
            FTPFile[] files = ftp.listFiles(ftpFileName);
            if (files == null || files.length == 0) {
                logger.info("远程文件不存在:" + ftpFileName);
                is = new FileInputStream(f);
            } else if (files != null && files.length == 1) {
                long remoteSize = files[0].getSize();
                logger.info("远程文件大小：" + remoteSize);
                if (localFileSize == remoteSize) {
                    logger.info("文件已成功上传，无需继续传输:" + ftpFileName);
                    return true;
                } else if (localFileSize < remoteSize) {
                    logger.error("远程文件大于本地文件:" + ftpFileName);
                    return true;
                }
                is = new FileInputStream(f);
                if (is.skip(remoteSize) == remoteSize)
                    ftp.setRestartOffset(remoteSize);
                logger.info("需要断点上传:" + ftpFileName);
            } else {
                logger.error("远程文件不唯一:" + ftpFileName);
            }
            logger.info("ftp文件上传开始:" + ftpFileName);
            boolean flag = ftp.storeFile(ftpFileName, is);
            if (!flag) {
                logger.error("FTP上传失败：" + ftpFileName);
                return false;
            } else
                logger.info("FTP上传成功：" + ftpFileName);
            ftp.logout();
        } catch (IOException e) {
            logger.error("FTP上传出现IO异常", e);
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("FTP上传出现IO异常", e);
                    return false;
                }
            }
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error("FTP上传出现IO异常", ioe);
                    return false;
                }
            }
        }
        return true;
    }
}
