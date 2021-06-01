package com.allinfinance.dev.ccs.dal.service;


import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;

import com.github.pagehelper.PageInfo;


/**
 * @author ：Lucas Li
 * @project :IntelliJ IDEA
 * @date ：2021/5/13 18:54
 * @description：用户持久层服务
 */

public interface TblUserService {

    public int deleteByPrimaryKey(String userId) ;

    public int deleteByPrimaryKey(UserReqParam userReqParam) ;

    public int insert(TblUser record);

    public int insertSelective(TblUser record);

    public TblUser selectByPrimaryKey(String userId) ;

    public int updateByPrimaryKeySelective(TblUser record) ;

    public int updateByPrimaryKey(TblUser record);

    public PageInfo<TblUser> pageSelectUsers(UserReqParam userReqParam) ;

    public TblUser selectCurrentUser(String  userName) ;

    public TblUser selectByNameAndOrg(UserReqParam userReqParam) ;
}
