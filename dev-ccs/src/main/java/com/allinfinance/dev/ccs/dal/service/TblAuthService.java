package com.allinfinance.dev.ccs.dal.service;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dto.AuthsQueryResponseDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @project: dev-parent
 * @description: 权限管理服务
 * @author: Lum Wang
 * @create: 2021-05-14 10:50
 */
public interface TblAuthService {
    int deleteByPrimaryKey(String authId);

    int insert(TblAuth record);

    int insertSelective(TblAuth record);

    TblAuth selectByPrimaryKey(String authId);

    int updateByPrimaryKeySelective(TblAuth record);

    int updateByPrimaryKey(TblAuth record);

    PageInfo<AuthsQueryResponseDTO> pageSelectAuths(AuthReqParam authReqParam);

    List<AuthsQueryResponseDTO> selectAuths(AuthReqParam authReqParam);

    List<TblMenu> selectMenus(String authId);

    TblAuth selectByAuthName(String authName);
}
