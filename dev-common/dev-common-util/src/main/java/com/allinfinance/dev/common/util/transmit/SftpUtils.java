package com.allinfinance.dev.common.util.transmit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author qipeng
 * @date 2022/9/13 9:36
 * @desc sftp上传下载文件工具类
 */
public class SftpUtils {
    private static final Logger logger = LoggerFactory.getLogger(SftpUtils.class);

    /**
     * 使用用户密码验证下载文件
     *
     * @param remoteIp   远程ip
     * @param remotePort 远程端口
     * @param username   用户名
     * @param password   密码
     * @param remotePath 远程路径
     * @param localPath  本地路径
     * @param fileName   文件名
     * @param timeout    连接超时时间
     * @return 下载成功返回true
     * @throws IOException 连接异常
     */
    public static boolean downloadWithPassword(String remoteIp, int remotePort, String username, String password,
                                               String remotePath, String localPath, String fileName, int timeout) throws IOException {
        SSHClient ssh = new SSHClient();
        ssh.setConnectTimeout(timeout);
        ssh.loadKnownHosts();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(remoteIp, remotePort);
        ssh.authPassword(username, password);
        try {

            try (SFTPClient sftp = ssh.newSFTPClient()) {
                String absoluteFile = StringUtils.isNotBlank(fileName) ? remotePath + fileName : remotePath;
                sftp.get(absoluteFile, new FileSystemFile(localPath));
            } catch (IOException e) {
                logger.error("文件下载过程中发生IO异常", e);
            }
        } finally {
            ssh.disconnect();
        }
        return true;
    }

    /**
     * 免密下载文件
     *
     * @param remoteIp   远程ip
     * @param remotePort 远程端口
     * @param username   用户名
     * @param remotePath 远程路径
     * @param localPath  本地路径
     * @param fileName   文件名
     * @param timeout    连接超时时间
     * @return 下载成功返回true
     * @throws IOException 连接异常
     */
    public static boolean downloadWithPublicKey(String remoteIp, int remotePort, String username, String remotePath, String localPath,
                                                String fileName, int timeout) throws IOException {
        SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        ssh.setConnectTimeout(timeout);
        ssh.loadKnownHosts();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(remoteIp, remotePort);
        try {
            ssh.authPublickey(username);
            try (SFTPClient sftp = ssh.newSFTPClient()) {
                String absoluteFile = StringUtils.isNotBlank(fileName) ? remotePath + fileName : remotePath;
                sftp.get(absoluteFile, new FileSystemFile(localPath));
            } catch (IOException e) {
                logger.error("文件下载过程中发生IO异常", e);
            }
        } finally {
            ssh.disconnect();
        }
        return true;
    }

    /**
     * 使用用户密码验证上传文件
     *
     * @param remoteIp   远程ip
     * @param remotePort 远程端口
     * @param username   用户名
     * @param password   密码
     * @param remotePath 远程路径
     * @param localPath  本地路径
     * @param fileName   文件名
     * @param timeout    连接超时时间
     * @return 上传成功返回true
     * @throws IOException 连接异常
     */
    public static boolean uploadWithPassword(String remoteIp, int remotePort, String username, String password,
                                             String remotePath, String localPath, String fileName, int timeout) throws IOException {
        SSHClient ssh = new SSHClient();
        ssh.setConnectTimeout(timeout);
        ssh.loadKnownHosts();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(remoteIp, remotePort);
        ssh.authPassword(username, password);
        try {
            try (SFTPClient sftp = ssh.newSFTPClient()) {
                String absoluteFile = StringUtils.isNotBlank(fileName) ? localPath + fileName : localPath;
                sftp.put(new FileSystemFile(absoluteFile), remotePath);
            } catch (IOException e) {
                logger.error("文件上传过程中发生IO异常", e);
            }
        } finally {
            ssh.disconnect();
        }

        return true;
    }

    /**
     * 免密上传文件
     *
     * @param remoteIp   远程ip
     * @param remotePort 远程端口
     * @param username   用户名
     * @param remotePath 远程路径
     * @param localPath  本地路径
     * @param fileName   文件名
     * @param timeout    连接超时时间
     * @return 上传成功返回true
     * @throws IOException 连接异常
     */
    public static boolean uploadWithPublicKey(String remoteIp, int remotePort, String username, String remotePath,
                                              String localPath, String fileName, int timeout) throws IOException {
        SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        ssh.setConnectTimeout(timeout);
        ssh.loadKnownHosts();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(remoteIp, remotePort);
        try {
            ssh.authPublickey(username);
            try (SFTPClient sftp = ssh.newSFTPClient()) {
                String absoluteFile = StringUtils.isNotBlank(fileName) ? localPath + fileName : localPath;
                sftp.put(new FileSystemFile(absoluteFile), remotePath);
            } catch (IOException e) {
                logger.error("文件上传过程中发生IO异常", e);
            }
        } finally {
            ssh.disconnect();
        }
        return true;
    }

}
