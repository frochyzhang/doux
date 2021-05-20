package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.mapper.TblUserMapper;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

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

    public TblUser selectCurrentUser(String  userNasme) ;
    public TblUser selectCurrentUser(String  userNasme,String org) ;
}
