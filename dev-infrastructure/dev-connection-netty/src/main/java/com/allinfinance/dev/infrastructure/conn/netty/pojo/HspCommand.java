package com.allinfinance.dev.infrastructure.conn.netty.pojo;

/**
 * @Description:
 * @Author: qipeng
 * @Date: 2022/7/14
 **/
public class HspCommand {
    /**
     * 长度域：2字节
     */
    private String lengthField;
    /**
     * 消息头：默认8字节，用来区分不同请求
     */
    private String messageHeader;
    /**
     * 命令码/应答码，1到2字节
     */
    private String code;
    /**
     * 请求内容
     */
    private String content;
    /**
     * 消息尾，暂时没用到
     */
    private String messageTail;

    public String getLengthField() {
        return lengthField;
    }

    public void setLengthField(String lengthField) {
        this.lengthField = lengthField;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageTail() {
        return messageTail;
    }

    public void setMessageTail(String messageTail) {
        this.messageTail = messageTail;
    }

    @Override
    public String toString() {
        return "HspCommand{" +
                "lengthField='" + lengthField + '\'' +
                ", messageHeader='" + messageHeader + '\'' +
                ", commandCode='" + code + '\'' +
                ", content='" + content + '\'' +
                ", messageTail='" + messageTail + '\'' +
                '}';
    }
}
