package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.mapper.TblAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuMapper;
import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.utils.IdUtils;
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
        record.setAuthId(IdUtils.getId());
        return tblAuthMapper.insertSelective(record);
    }

    public TblAuth selectByPrimaryKey(String authId){
        return tblAuthMapper.selectByPrimaryKey(authId);
    }

    public int updateByPrimaryKeySelective(TblAuth record){
        return tblAuthMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblAuth record){
        return tblAuthMapper.updateByPrimaryKeySelective(record);
    }

    public PageInfo<TblAuth> pageSelectAuths(AuthReqParam authReqParam) {
        PageHelper.startPage(authReqParam.getCurrent(),authReqParam.getPageSize());
        List<TblAuth> auths = tblAuthMapper.pageSelectAuths(authReqParam);
        return new PageInfo<TblAuth>(auths);
    }

    public List<TblAuth> selectAuths(AuthReqParam authReqParam) {
        return tblAuthMapper.selectAuths(authReqParam);
    }

    public List<TblMenuAuth> selectMenuAuths(){
        return tblMenuAuthMapper.selectMenuAuths();
    }

    public List<TblMenu> selectMenus(String authId) {
        return tblMenuMapper.selectMenus(authId);
    }

    public int deleteMenuAuths(String authId) {
        return tblMenuAuthMapper.deleteMenuAuths(authId);
    }

    public int insertMenuAuth(TblMenuAuth record) {
        return tblMenuAuthMapper.insertSelective(record);
    }

    public int invalidateAuth(String authId) {
        String isAvailable = AosContent.IS_AVAILABLE_FALSE;
        return tblAuthMapper.invalidateAuth(authId,isAvailable);
    }
}
