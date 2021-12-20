package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.mapper.TblRoleAuthMapper;
import com.allinfinance.dev.ccs.dal.model.TblMenuAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuth;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @project: dev-parent
 * @description: 角色权限管理
 * @author: Lum Wang
 * @create: 2021-05-14 11:50
 */
 public interface TblRoleAuthService {


     int deleteByPrimaryKey(TblRoleAuthKey key) ;

     int insert(TblRoleAuth record) ;

     int insertSelective(TblRoleAuth record) ;

     TblRoleAuth selectByPrimaryKey(TblRoleAuthKey key) ;

     int updateByPrimaryKeySelective(TblRoleAuth record) ;

     int updateByPrimaryKey(TblRoleAuth record) ;


     List<TblRoleAuth> selectRoleAuths() ;

     int deleteByRoleId(String roleId);

     List<TblRoleAuth> selectOnUseAuths(AuthReqParam authReqParam);

}
