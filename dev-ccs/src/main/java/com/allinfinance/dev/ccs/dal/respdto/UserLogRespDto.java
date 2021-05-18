package com.allinfinance.dev.ccs.dal.respdto;

import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;

/**
 * @author ：Lucas Li
 * @project :dev-parent
 * @date ：2021/5/18 18:25
 * @description：日志列表返回DTO
 */
public class UserLogRespDto extends TblUserOptLog {
    private  String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
