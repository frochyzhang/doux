package cn.lezoo.doux.common.util.transmit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author qipeng
 * @date 2022/9/13 9:36
 * @desc sftp上传下载文件工具类
 */
public class SftpUtils {
    private static final Logger logger = LoggerFactory.getLogger(SftpUtils.class);

    /**
     * 下载文件
     *
     * @param remoteIp       远程ip
     * @param remotePort     远程端口
     * @param username       用户名
     * @param password       密码
     * @param remotePath     远程路径
     * @param localPath      本地路径
     * @param fileName       文件名
     * @param timeout        连接超时时间(ms)
     * @param isPasswordFree 是否使用免密登录
     * @return 下载成功返回true
     */
    public static boolean download(String remoteIp, int remotePort, String username, String password, String remotePath,
                                   String localPath, String fileName, int timeout, boolean isPasswordFree) {
        logger.info("开始下载文件...");
        SFTPClient sftpClient;
        try {
            sftpClient = getSftpClient(remoteIp, remotePort, username, password, timeout, isPasswordFree);
        } catch (IOException e) {
            logger.error("建立连接远程异常，请检查远程连接配置，remoteIp：{}，remotePort：{}，username：{}，password：{}，是否免密：{}",
                    remoteIp, remotePort, username, password, isPasswordFree, e);
            return false;
        }
        String remoteAbsoluteFile = StringUtils.endsWith(remotePath, "/") ? remotePath + fileName : remotePath + "/" + fileName;
        try {
            long previousSize = sftpClient.lstat(remoteAbsoluteFile).getSize();
            TimeUnit.MILLISECONDS.sleep(100);
            long latestSize = sftpClient.lstat(remoteAbsoluteFile).getSize();
            if (previousSize != latestSize) {
                logger.error("文件大小发生变化，停止下载");
                return false;
            }
            sftpClient.get(remoteAbsoluteFile, new FileSystemFile(localPath));
        } catch (IOException e) {
            logger.error("下载远程文件异常，请检查文件路径，remotePath：{}，localPath：{}，fileName：{}", remotePath, localPath, fileName, e);
            return false;
        } catch (InterruptedException e) {
            logger.error("判断文件状态异常", e);
            return false;
        } finally {
            if (sftpClient != null) {
                try {
                    sftpClient.close();
                } catch (IOException e) {
                    logger.error("SFTPClient关闭异常", e);
                }
            }
        }
        logger.info("下载文件成功！");
        return true;
    }

    /**
     * 上传文件
     *
     * @param remoteIp       远程ip
     * @param remotePort     远程端口
     * @param username       用户名
     * @param password       密码
     * @param remotePath     远程路径
     * @param localPath      本地路径
     * @param fileName       文件名
     * @param timeout        连接超时时间(ms)
     * @param isPasswordFree 是否使用免密登录
     * @return 上传成功返回true
     */
    public static boolean upload(String remoteIp, int remotePort, String username, String password, String remotePath,
                                 String localPath, String fileName, int timeout, boolean isPasswordFree) {
        logger.info("开始上传文件...");
        SFTPClient sftpClient;
        try {
            sftpClient = getSftpClient(remoteIp, remotePort, username, password, timeout, isPasswordFree);
        } catch (IOException e) {
            logger.error("建立连接远程异常，请检查远程连接配置，remoteIp：{}，remotePort：{}，username：{}，password：{}，是否免密：{}",
                    remoteIp, remotePort, username, password, isPasswordFree, e);
            return false;
        }

        String localAbsoluteFile = StringUtils.endsWith(localPath, "/") ? localPath + fileName : localPath + "/" + fileName;
        try {
            sftpClient.put(new FileSystemFile(localAbsoluteFile), remotePath);
        } catch (IOException e) {
            logger.error("上传本地文件异常，请检查文件路径，remotePath：{}，localPath：{}，fileName：{}", remotePath, localPath, fileName, e);
            return false;
        } finally {
            if (sftpClient != null) {
                try {
                    sftpClient.close();
                } catch (IOException e) {
                    logger.error("SFTPClient关闭异常", e);
                }
            }
        }
        logger.info("上传文件成功！");
        return true;
    }

    /**
     * 获取sftp客户端对象
     *
     * @param remoteIp       远程ip
     * @param remotePort     远程短裤
     * @param username       用户名
     * @param password       密码（免密登录时，可不填）
     * @param timeout        建立连接的超时时间
     * @param isPasswordFree 是否使用免密登录
     * @return SftpClient
     * @throws IOException 连接异常或获取sftp客户端异常是抛出
     */
    private static SFTPClient getSftpClient(String remoteIp, int remotePort, String username, String password,
                                            int timeout, boolean isPasswordFree) throws IOException {
        logger.debug("开始建立ssh连接，并获取sftp客户端...");
        SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        ssh.setConnectTimeout(timeout);
        ssh.loadKnownHosts();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(remoteIp, remotePort);

        if (isPasswordFree) {
            if (logger.isDebugEnabled()) {
                logger.debug("使用ssh免密登录");
            }
            ssh.authPublickey(username);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("使用密码验证登录");
            }
            ssh.authPassword(username, password);
        }
        SFTPClient sftpClient = ssh.newSFTPClient();
        if (logger.isDebugEnabled()) {
            logger.debug("获取sftp客户端成功！");
        }
        return sftpClient;
    }
}
