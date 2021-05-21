package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.mapper.TblAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuMapper;
import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @project: dev-parent
 * @description: 权限管理服务
 * @author: Lum Wang
 * @create: 2021-05-14 10:50
 */
@Service
public class TblAuthService {

    @Autowired
    private TblAuthMapper tblAuthMapper;

    @Autowired
    private TblMenuAuthMapper tblMenuAuthMapper;

    @Autowired
    private TblMenuMapper tblMenuMapper;

    public int deleteByPrimaryKey(String authId){
        return tblAuthMapper.deleteByPrimaryKey(authId);
    }

    public int insert(TblAuth record){
        return tblAuthMapper.insert(record);
    }

    public int insertSelective(TblAuth record){
        return tblAuthMapper.insertSelective(record);
    }

    public TblAuth selectByPrimaryKey(String authId){
        return tblAuthMapper.selectByPrimaryKey(authId);
    }

    public int updateByPrimaryKeySelective(TblAuth record){
        return tblAuthMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblAuth record){
        return tblAuthMapper.updateByPrimaryKey(record);
    }

    public PageInfo<TblAuth> pageSelectAuths(AuthReqParam authReqParam) {
        PageHelper.startPage(authReqParam.getCurrent(),authReqParam.getPageSize());
        List<TblAuth> auths = tblAuthMapper.pageSelectAuths(authReqParam);
        return new PageInfo<TblAuth>(auths);
    }

    public List<TblAuth> selectAuths() {
        return tblAuthMapper.selectAuths();
    }

    public List<TblMenuAuth> selectMenuAuths(){
        return tblMenuAuthMapper.selectMenuAuths();
    }

    public List<TblMenu> selectMenus() {
        return tblMenuMapper.selectMenus();
    }
}
