package com.allinfinance.dev.ccs.dal.service;


import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;

import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 * @author ：Lucas Li
 * @project :IntelliJ IDEA
 * @date ：2021/5/13 18:54
 * @description：用户持久层服务
 */

public interface TblUserService {

    int deleteByPrimaryKey(String userId);

    int deleteByPrimaryKey(UserReqParam userReqParam);

    int insert(TblUser record);

    int insertSelective(TblUser record);

    TblUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(TblUser record);

    int updateByPrimaryKey(TblUser record);

    PageInfo<TblUser> pageSelectUsers(UserReqParam userReqParam);

    TblUser selectCurrentUser(String userName);

    TblUser selectByUserName(UserReqParam userReqParam);

    List<TblUser> selectOnUseRoles(RoleReqParam roleReqParam);
}
