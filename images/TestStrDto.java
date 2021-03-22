package com.allinfinance.dev.batch.task;

/**
 * @author 张勇
 * @description
 * @date 2020/12/10 15:10
 */
public class TestStrDto {
    private String orgId;
    private String serialNo;
    private String tel;
    private String content;
    private String msgBegin;
    private String msgEnd;
    private String respCd;
    private String respDesc;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgBegin() {
        return msgBegin;
    }

    public void setMsgBegin(String msgBegin) {
        this.msgBegin = msgBegin;
    }

    public String getMsgEnd() {
        return msgEnd;
    }

    public void setMsgEnd(String msgEnd) {
        this.msgEnd = msgEnd;
    }

    public String getRespCd() {
        return respCd;
    }

    public void setRespCd(String respCd) {
        this.respCd = respCd;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    @Override
    public String toString() {
        return "TestStrDto{" +
                "orgId='" + orgId + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", tel='" + tel + '\'' +
                ", content='" + content + '\'' +
                ", msgBegin='" + msgBegin + '\'' +
                ", msgEnd='" + msgEnd + '\'' +
                ", respCd='" + respCd + '\'' +
                ", respDesc='" + respDesc + '\'' +
                '}';
    }
}
