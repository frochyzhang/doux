package com.allinfinance.dev.common.api.transmit.dto;

/**
 * @author qipeng
 * @date 2022/9/9 15:28
 * @desc 文件传输所需的请求参数
 */
public class TransmitRequestDTO {
    /**
     * 传输方式：ftp、sftp
     */
    private TransmitMode transmitMode;
    /**
     * 服务使用方式：仅上传、仅下载、上传＋下载
     */
    private UsingMethod usingMethod;
    /**
     * 远程源信息，download和both必填
     */
    private RemoteMessage source;
    /**
     * 远程目标信息，upload和both必填
     */
    private RemoteMessage target;
    /**
     * 文件名
     */
    private String fileName;

    public static class RemoteMessage {
        /**
         * 远程用户ip
         */
        private String ip;
        /**
         * 远程用户端口
         */
        private Integer port;
        /**
         * 远程用户名
         */
        private String user;
        /**
         * 远程用户密码
         */
        private String password;
        /**
         * 远程文件路径
         */
        private String path;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    @Override
    public String toString() {
        return "TransmitRequestDTO{" +
                "transmitMode=" + transmitMode +
                ", usingMethod=" + usingMethod +
                ", source=" + source +
                ", target=" + target +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
