package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.dal.mapper.TblUserMapper;
import com.allinfinance.dev.ccs.dal.model.TblUser;
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
    public int deleteByPrimaryKey(String userId) {
        return tblUserMapper.deleteByPrimaryKey(userId);
    }

    public int deleteByPrimaryKey(Integer userId) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(UserReqParam userReqParam) {
        //逻辑删除，只是说将用户变为不可用
        TblUserOptLog optLog = new TblUserOptLog();
//        int res = tblUserOptLogMapper.insertSelective(optLog);
//        assert res == 0;
        int i = 1;
        for (String userId : userReqParam.getUserIds()) {
            i = deleteByPrimaryKey(userId);
            //断言 如果存在更新失败，则抛出异常？？
            assert i == 0;
        }
        return i;
    }

    @Override
    public int insert(TblUser record) {
        return tblUserMapper.insert(record);
    }

    @Override
    public int insertSelective(TblUser record) {
        //添加当前系统时间为新增用户的创建时间
        record.setCreateTime(new Date());
        record.setUserId(IdUtils.getId());
        return tblUserMapper.insertSelective(record);
    }


    public TblUser selectByPrimaryKey(Integer userId) {
        return null;
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
