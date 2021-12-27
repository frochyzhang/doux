package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.converter.ResponseMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblMenuMapper;
import com.allinfinance.dev.ccs.dal.model.*;
import com.allinfinance.dev.ccs.dal.paramvo.AuthReqParam;
import com.allinfinance.dev.ccs.dal.service.TblAuthService;
import com.allinfinance.dev.ccs.dto.AuthsQueryResponseDTO;
import com.allinfinance.dev.ccs.utils.IdUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @project: dev-parent
 * @description: 权限管理服务
 * @author: Lum Wang
 * @create: 2021-05-14 10:50
 */
@Service
public class TblAuthServiceImpl implements TblAuthService {

    @Autowired
    private TblAuthMapper tblAuthMapper;

    @Autowired
    private TblMenuAuthMapper tblMenuAuthMapper;

    @Autowired
    private TblMenuMapper tblMenuMapper;

    @Override
    public int deleteByPrimaryKey(String authId) {
        return tblAuthMapper.deleteByPrimaryKey(authId);
    }

    @Override
    public int insert(TblAuth record) {
        return tblAuthMapper.insert(record);
    }

    @Override
    public int insertSelective(TblAuth record) {
        record.setAuthId(IdUtils.getId());
        return tblAuthMapper.insertSelective(record);
    }

    @Override
    public TblAuth selectByPrimaryKey(String authId) {
        return tblAuthMapper.selectByPrimaryKey(authId);
    }

    @Override
    public int updateByPrimaryKeySelective(TblAuth record) {
        return tblAuthMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TblAuth record) {
        return tblAuthMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public PageInfo<AuthsQueryResponseDTO> pageSelectAuths(AuthReqParam authReqParam) {
        PageHelper.startPage(authReqParam.getCurrent(), authReqParam.getPageSize());
        TblAuthExample tblAuthExample = new TblAuthExample();
        TblAuthExample.Criteria criteria = tblAuthExample.createCriteria();
        if (StringUtils.isNotBlank(authReqParam.getAuthName())){
            criteria.andAuthNameLike("%" + authReqParam.getAuthName() + "%");
        }
        if (StringUtils.isNotBlank(authReqParam.getOrg())) {
            criteria.andOrgEqualTo(authReqParam.getOrg());
        }
        if (StringUtils.isNotBlank(authReqParam.getIsAvailable())) {
            criteria.andIsAvailableEqualTo(authReqParam.getIsAvailable());
        }
        List<AuthsQueryResponseDTO> authsQueryResponseDTOList = tblAuthMapper.selectByExample(tblAuthExample)
                .stream()
                .map(ResponseMapper.INSTANCE::convertToAuthsQueryResponseDTO)
                .collect(Collectors.toList());
        return new PageInfo<>(authsQueryResponseDTOList);
    }

    @Override
    public List<AuthsQueryResponseDTO> selectAuths(AuthReqParam authReqParam) {
        TblAuthExample tblAuthExample = new TblAuthExample();
        TblAuthExample.Criteria criteria = tblAuthExample.createCriteria();
        if (StringUtils.isNotBlank(authReqParam.getAuthName())){
            criteria.andAuthNameLike("%" + authReqParam.getAuthName() + "%");
        }
        if (StringUtils.isNotBlank(authReqParam.getOrg())) {
            criteria.andOrgEqualTo(authReqParam.getOrg());
        }
        if (StringUtils.isNotBlank(authReqParam.getIsAvailable())) {
            criteria.andIsAvailableEqualTo(authReqParam.getIsAvailable());
        }
        return tblAuthMapper.selectByExample(tblAuthExample)
                .stream()
                .map(ResponseMapper.INSTANCE::convertToAuthsQueryResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TblMenu> selectMenus(String authId) {
        return tblMenuMapper.selectByExample(new TblMenuExample())
                .stream()
                .map(tblMenu -> {
                    TblMenuAuthExample tblMenuAuthExample = new TblMenuAuthExample();
                    tblMenuAuthExample.createCriteria()
                            .andAuthIdEqualTo(authId)
                            .andMenuIdEqualTo(tblMenu.getMenuId());
                    TblMenuAuth tblMenuAuth = tblMenuAuthMapper.selectByExample(tblMenuAuthExample)
                            .stream()
                            .findFirst()
                            .orElse(null);
                    if (tblMenuAuth == null) {
                        tblMenu.setReservedField1(AosContent.IS_USE_N);
                    } else {
                        tblMenu.setReservedField1(AosContent.IS_USE_Y);
                    }
                    return tblMenu;
                }).collect(Collectors.toList());
    }

    @Override
    public TblAuth selectByAuthName(String authName) {
        TblAuthExample tblAuthExample = new TblAuthExample();
        tblAuthExample.createCriteria()
                .andAuthNameEqualTo(authName);
        return tblAuthMapper.selectByExample(tblAuthExample)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
