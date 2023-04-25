package com.allinfinance.dev.common.transmit.api.dto;

/**
 * @author qipeng
 * @date 2022/9/9 15:28
 * @desc 文件传输所需的请求参数
 */
public class TransmitRequestDTO {
    /**
     * 远程源信息，download和both必填
     */
    private RemoteMessage source;
    /**
     * 远程目标信息，upload和both必填
     */
    private RemoteMessage target;
    /**
     * 本地文件存放路径
     */
    private String localPath;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 超时时间(ms)
     */
    private Integer timeout = 3000;
    /**
     * localPath是否为追加目录
     * true则在默认目录基础上追加localPath
     * false则(localPath)为自定义目录
     */
    private Boolean append;

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
        private String username;
        /**
         * 远程用户密码
         */
        private String password;
        /**
         * 远程文件路径
         */
        private String path;
        /**
         * 传输方式：ftp、sftp
         */
        private TransmitMode transmitMode;

        public TransmitMode getTransmitMode() {
            return transmitMode;
        }

        public void setTransmitMode(TransmitMode transmitMode) {
            this.transmitMode = transmitMode;
        }

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        @Override
        public String toString() {
            return "RemoteMessage{" +
                    "ip='" + ip + '\'' +
                    ", port=" + port +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", path='" + path + '\'' +
                    ", transmitMode=" + transmitMode +
                    '}';
        }
    }

    public Boolean getAppend() {
        return append;
    }

    public void setAppend(Boolean append) {
        this.append = append;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public RemoteMessage getSource() {
        return source;
    }

    public void setSource(RemoteMessage source) {
        this.source = source;
    }

    public RemoteMessage getTarget() {
        return target;
    }

    public void setTarget(RemoteMessage target) {
        this.target = target;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "TransmitRequestDTO{" +
                "source=" + source +
                ", target=" + target +
                ", localPath='" + localPath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
