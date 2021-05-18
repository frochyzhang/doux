package com.allinfinance.dev.ccs.dal.paramvo;

import lombok.Data;

import java.io.Serializable;
@Data
public class LoginParam implements Serializable {
    private  String userName;
    private  String userPass;
}
