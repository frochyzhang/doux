package com.allinfinance.dev.core.util.transmit;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * SftpUtils
 *
 * @author hongmr
 * @date 2017/2/22
 */
public class SftpUtils {
    private static Logger logger = LoggerFactory.getLogger(SftpUtils.class);

    /**
     * 连接sftp服务器，获得连接会话
     *
     * @param host
     *            服务器名
     * @param port
     *            端口号
     * @param username
     *            用户名
     * @param password
     *            密码
     *
     * @return SHH对话SESSION
     *
     * @throws JSchException
     *             连接失败
     */
    public static Session connect(String host, int port, String username,
                                  String password) throws JSchException {
        Session session = null;
        try {
            JSch jsch = new JSch();
            // 取得对话
            jsch.getSession(username, host, port);
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            // 连接对话
            session.connect();
        } catch (JSchException e) {
            logger.error("[" + host + ":" + port + "]" + "SFTP连接失败", e);
            throw e;
        }
        return session;
    }

    /**
     * 上传文件
     *
     * @param remotePath
     *            远程文件路径
     * @param remoteFileName
     *            远程文件名
     * @param localFileFullName
     *            本地文件名（包含路径）
     * @param channelSftp
     *            SFTP通道
     *
     * @throws SftpException
     *             SFTP通信异常
     * @throws IOException
     *             本地IO异常
     */
    public static boolean uploadFile(String remotePath, String remoteFileName,
                              String localFileFullName, ChannelSftp channelSftp)
            throws SftpException, IOException {
        FileInputStream fileInputStream = null;
        boolean flag = true;
        try {
            fileInputStream = new FileInputStream(localFileFullName);
            if (StringUtils.isNotEmpty(remotePath)) {
                try {
                    channelSftp.cd(remotePath);
                } catch (SftpException e) {
                    logger.info("目标地址目录不存在，创建目标地址目录" + remotePath);
                    channelSftp.mkdir(remotePath);
                    channelSftp.cd(remotePath);
                }
            }
            channelSftp.put(fileInputStream, remoteFileName);
        } catch(Exception e){
            e.printStackTrace();
            flag = false;
        }
        finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        return flag;
    }

    /**
     * 下载文件
     *
     * @param remotePath
     *            远程文件路径
     * @param remoteFileName
     *            远程文件名
     * @param localFileFullName
     *            本地文件名（包含路径）
     * @param channelSftp
     *            SFTP通道
     *
     * @throws SftpException
     *             SFTP通信异常
     * @throws IOException
     *             本地IO异常
     */
    public static boolean downloadFile(String remotePath, String remoteFileName,
                                String localFileFullName, ChannelSftp channelSftp)
            throws SftpException, IOException {
        FileOutputStream fileOutputStream = null;
        boolean flag = true;
        try {
            fileOutputStream = new FileOutputStream(localFileFullName);
            if (StringUtils.isNotEmpty(remotePath)) {
                channelSftp.cd(remotePath);
            }
            channelSftp.get(remoteFileName, fileOutputStream);
        }catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }finally
        {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param host
     *            服务器名
     * @param port
     *            端口号
     * @param username
     *            用户名
     * @param password
     *            密码
     * @param remotePath
     *            远程文件路径
     * @param remoteFileName
     *            远程文件名
     * @param localFileFullName
     *            本地文件名（包含路径）
     *
     * @throws JSchException
     *             连接失败
     * @throws SftpException
     *             SFTP通信异常
     * @throws IOException
     *             本地IO异常
     */
    public static boolean upload(String host, int port, String username,
                              String password, String remotePath, String remoteFileName,
                              String localFileFullName) throws JSchException, SftpException, IOException {
        Session session = null;
        ChannelSftp channel = null;
        boolean flag = true;
        try {
            // 打开SFTP通道
            session = connect(host, port, username, password);
            if (session == null) {
                flag = false;
            }
            // 打开SFTP通道
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            // 上传文件
            if (!uploadFile(remotePath, remoteFileName, localFileFullName, channel)) {
                flag = false;
            }
        } catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return flag;
    }

    /**
     * 下载文件
     *
     * @param host
     *            服务器名
     * @param port
     *            端口号
     * @param username
     *            用户名
     * @param password
     *            密码
     * @param remotePath
     *            远程文件路径
     * @param remoteFileName
     *            远程文件名
     * @param localFileFullName
     *            本地文件名（包含路径）
     *
     * @throws JSchException
     *             连接失败
     * @throws SftpException
     *             SFTP通信异常
     * @throws IOException
     *             本地IO异常
     */
    public static boolean download(String host, int port, String username,
                                String password, String remotePath, String remoteFileName,
                                String localFileFullName) throws JSchException, SftpException, IOException {
        Session session = null;
        ChannelSftp channel = null;
        boolean flag = true;
        try {
            // 获得SESSION
            session = connect(host, port, username, password);
            if (session == null) {
                flag = false;
            }
            channel = (ChannelSftp) session.openChannel("sftp");
            // 打开SFTP通道
            channel.connect();
            // 下载文件
            if (!downloadFile(remotePath, remoteFileName, localFileFullName, channel)) {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        finally
        {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return flag;
    }

    public static void main(String[] args) throws Exception {
        boolean flag = SftpUtils.upload("10.250.3.34",22,"qpp","qpp","","XQL01110023","/Users/hongmr/Work/Temp/XQL0111002");
        System.out.println("flag:" + flag);
    }
}
