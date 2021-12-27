package com.allinfinance.dev.ccs.dal.service;


import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.model.TblUserExample;
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
    long countByExample(TblUserExample example);

    int deleteByPrimaryKey(String userId);

    int insert(TblUser record);

    int insertSelective(TblUser record);

    List<TblUser> selectByExample(TblUserExample example);

    TblUser selectByPrimaryKey(String userId);

    int updateByExampleSelective(TblUser record, TblUserExample example);

    int updateByExample(TblUser record, TblUserExample example);

    int updateByPrimaryKeySelective(TblUser record);

    int updateByPrimaryKey(TblUser record);

    public PageInfo<TblUser> pageSelectUsers(UserReqParam userReqParam) ;

    public TblUser selectCurrentUser(String  userName) ;

    public TblUser selectByNameAndOrg(UserReqParam userReqParam) ;

    List<TblUser> selectOnUseRoles(RoleReqParam roleReqParam);
}
