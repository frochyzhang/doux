package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.content.AosContent;
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
import org.apache.commons.lang3.StringUtils;
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
    public int deleteByPrimaryKey(String userId) {
        return tblUserMapper.deleteByPrimaryKey(userId);
    }

    public int deleteByPrimaryKey(Integer userId) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(UserReqParam userReqParam) {
        //逻辑删除，将用户变为不可用
        TblUserExample example = new TblUserExample();
        TblUserExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdIn(Arrays.asList(userReqParam.getUserIds()));
        TblUser tblUser = new TblUser();
        tblUser.setIsAvailable(AosContent.IS_AVAILABLE_FALSE);
        return tblUserMapper.updateByExampleSelective(tblUser, example);
    }

    @Override
    public int insert(TblUser record) {
        return tblUserMapper.insert(record);
    }

    @Override
    public int insertSelective(TblUser record) {
        record.setUserId(IdUtils.getId());
        return tblUserMapper.insertSelective(record);
    }

    @Override
    public TblUser selectByPrimaryKey(String userId) {
        return tblUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(TblUser record) {
        record.setUpdateTime(new Date());
        return tblUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TblUser record) {
        return tblUserMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<TblUser> pageSelectUsers(UserReqParam userReqParam) {
        PageHelper.startPage(userReqParam.getCurrent(), userReqParam.getPageSize());
        TblUserExample example = new TblUserExample();
        TblUserExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(userReqParam.getRoleId())) {
            criteria.andRoleIdEqualTo(userReqParam.getRoleId());
        }
        if (StringUtils.isNotBlank(userReqParam.getOrg())) {
            criteria.andOrgEqualTo(userReqParam.getOrg());
        }
        if (StringUtils.isNotBlank(userReqParam.getUserName())) {
            criteria.andUserIdEqualTo(userReqParam.getUserName());
        }
        List<TblUser> users = tblUserMapper.selectByExample(example);
        return new PageInfo<>(users);
    }

    @Override
    public TblUser selectCurrentUser(String userName) {
        TblUserExample example = new TblUserExample();
        TblUserExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userName);
        return tblUserMapper.selectByExample(example).stream().findFirst().orElse(null);
    }


    @Override
    public TblUser selectByUserName(UserReqParam userReqParam) {
        TblUserExample example = new TblUserExample();
        TblUserExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userReqParam.getUserName());
        return tblUserMapper.selectByExample(example).stream().findFirst().orElse(null);
    }

    @Override
    public List<TblUser> selectOnUseRoles(RoleReqParam roleReqParam) {
        TblUserExample example = new TblUserExample();
        TblUserExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdIn(Arrays.asList(roleReqParam.getRoleIds()));
        // 当前可用的用户
        criteria.andIsAvailableEqualTo(AosContent.IS_AVAILABLE_TRUE);
        return tblUserMapper.selectByExample(example);
    }
}
