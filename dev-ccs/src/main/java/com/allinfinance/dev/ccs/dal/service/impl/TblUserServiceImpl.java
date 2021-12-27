package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.dal.mapper.TblUserMapper;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.model.TblUserExample;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.utils.IdUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author ：Lucas Li
 * @project :IntelliJ IDEA
 * @date ：2021/5/13 18:54
 * @description：用户持久层服务
 */
@Service
public class TblUserServiceImpl implements TblUserService {

    @Autowired
    private TblUserMapper tblUserMapper;

    @Override
    public long countByExample(TblUserExample example) {
        return tblUserMapper.countByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(String userId) {
        return tblUserMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int insert(TblUser record) {
        return tblUserMapper.insert(record);
    }

    @Override
    public int insertSelective(TblUser record) {
        return tblUserMapper.insertSelective(record);
    }

    @Override
    public List<TblUser> selectByExample(TblUserExample example) {
        return tblUserMapper.selectByExample(example);
    }

    @Override
    public TblUser selectByPrimaryKey(String userId) {
        return tblUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateByExampleSelective(TblUser record, TblUserExample example) {
        return tblUserMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(TblUser record, TblUserExample example) {
        return tblUserMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(TblUser record) {
        return tblUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TblUser record) {
        return tblUserMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<TblUser> pageSelectUsers(UserReqParam userReqParam) {
        PageHelper.startPage(userReqParam.getCurrent(), userReqParam.getPageSize());
        List<TblUser> users = tblUserMapper.pageSelectUsers(userReqParam);
        return new PageInfo<TblUser>(users);
    }

    @Override
    public TblUser selectCurrentUser(String userNasme) {
        return tblUserMapper.selectByUserName(userNasme);
    }

    @Override
    public TblUser selectByNameAndOrg(UserReqParam userReqParam) {
       return tblUserMapper.selectByNameAndOrg(userReqParam);
    }

    @Override
    public List<TblUser> selectOnUseRoles(RoleReqParam roleReqParam) {
        return tblUserMapper.selectUsersByRoleIds(new ArrayList<>(Arrays.asList(roleReqParam.getRoleIds())));
    }
}
