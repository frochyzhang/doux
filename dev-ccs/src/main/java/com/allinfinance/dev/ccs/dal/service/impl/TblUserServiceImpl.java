package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.dal.mapper.TblUserMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblUserOptLogMapper;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.model.TblUserOptLog;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.OperTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private TblUserOptLogMapper tblUserOptLogMapper;

    public int deleteByPrimaryKey(String userId) {
        return tblUserMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int deleteByPrimaryKey(Integer userId) {
        return 0;
    }

    public int deleteByPrimaryKey(UserReqParam userReqParam) {
        //逻辑删除，只是说将用户变为不可用
        TblUserOptLog optLog = new TblUserOptLog();
        optLog.setInterviewtTime(new Date());
        optLog.setUserId("10.250.2.7");
        // optLog.setColumnName();
        optLog.setOperationPath(userReqParam.getReservedField1());
        optLog.setTableName("TBL_USER");
        optLog.setOperationType(OperTypeEnum.UPDATE.code());
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

    public int insert(TblUser record) {
        return tblUserMapper.insert(record);
    }

    public int insertSelective(TblUser record) {
        //添加当前系统时间为新增用户的创建时间
        record.setCreateTime(new Date());
        return tblUserMapper.insertSelective(record);
    }

    @Override
    public TblUser selectByPrimaryKey(Integer userId) {
        return null;
    }

    public TblUser selectByPrimaryKey(String userId) {
        return tblUserMapper.selectByPrimaryKey(userId);
    }

    public int updateByPrimaryKeySelective(TblUser record) {
        record.setUpdateTime(new Date());
        //获取当前操作员(待定)....
        //根据Id获取原用户信息比较后更新


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

    @Override
    public List<TblUser> SelectUsers(UserReqParam userReqParam) {
       return tblUserMapper.selectByNameAndOrg();
    }
}
