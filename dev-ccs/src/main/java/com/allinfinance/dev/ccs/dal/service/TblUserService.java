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
@Service
public class TblUserService {

    @Autowired
    private TblUserMapper tblUserMapper;

    public int deleteByPrimaryKey(Integer userId) {
        return tblUserMapper.deleteByPrimaryKey(userId);
    }

    public int deleteByPrimaryKey(Integer[] userIds) {
        //逻辑删除，只是说将用户变为不可用
        int i = 1;
        for (Integer userId : userIds) {
            i = deleteByPrimaryKey(userId);
            //断言 阮国存在更新失败，则跑出异常？？
            assert i == 0;
        }
        return i;
    }

    public int insert(TblUser record) {
        return tblUserMapper.insert(record);
    }

    public int insertSelective(TblUser record) {
        //添加当前系统时间为新增用户的创建时间
        record.setCreateTime(new Date());
        return tblUserMapper.insertSelective(record);
    }

    public TblUser selectByPrimaryKey(Integer userId) {
        return tblUserMapper.selectByPrimaryKey(userId);
    }

    public int updateByPrimaryKeySelective(TblUser record) {
        return tblUserMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblUser record) {
        return tblUserMapper.updateByPrimaryKey(record);
    }

    public PageInfo<TblUser> pageSelectUsers(UserReqParam userReqParam) {
        PageHelper.startPage(userReqParam.getCurrent(), userReqParam.getPageSize());
        List<TblUser> users = tblUserMapper.pageSelectUsers(userReqParam);
        return new PageInfo<TblUser>(users);
    }
}
