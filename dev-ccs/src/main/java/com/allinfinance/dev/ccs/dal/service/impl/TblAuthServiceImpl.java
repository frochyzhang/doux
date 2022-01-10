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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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
        // 需要将权限和菜单对应关系的中间表也删除
        TblMenuAuthExample example = new TblMenuAuthExample();
        example.createCriteria().andAuthIdEqualTo(record.getAuthId());
        tblMenuAuthMapper.deleteByExample(example);
        // 将权限置为不可用
        return tblAuthMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public PageInfo<AuthsQueryResponseDTO> pageSelectAuths(AuthReqParam authReqParam) {
        PageHelper.startPage(authReqParam.getCurrent(), authReqParam.getPageSize());
        TblAuthExample tblAuthExample = new TblAuthExample();
        TblAuthExample.Criteria criteria = tblAuthExample.createCriteria();
        if (StringUtils.isNotBlank(authReqParam.getAuthName())) {
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
        if (StringUtils.isNotBlank(authReqParam.getAuthName())) {
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
        TblMenuAuthExample tblMenuAuthExample = new TblMenuAuthExample();
        tblMenuAuthExample.createCriteria()
                .andAuthIdEqualTo(authId);
        List<String> menuIdList = tblMenuAuthMapper.selectByExample(tblMenuAuthExample)
                .stream()
                .map(TblMenuAuthKey::getMenuId)
                .collect(Collectors.toList());
        TblMenuExample tblMenuExample = new TblMenuExample();
        tblMenuExample.createCriteria()
                .andMenuIdIn(menuIdList);
        return tblMenuMapper.selectByExample(tblMenuExample)
                .stream()
                .flatMap(tblMenu -> {
                    if (AosContent.LEVEL_1.equals(tblMenu.getNodeType())) {
                        tblMenuExample.clear();
                        tblMenuExample.createCriteria()
                                .andParentMidLike(tblMenu.getMenuId() + "%");
                        List<TblMenu> tblMenuList = tblMenuMapper.selectByExample(tblMenuExample);
                        tblMenuList.add(tblMenu);
                        return tblMenuList.stream();
                    } else if (AosContent.LEVEL_2.equals(tblMenu.getNodeType())) {
                        tblMenuExample.clear();
                        tblMenuExample.createCriteria()
                                .andMenuIdEqualTo(tblMenu.getMenuId().substring(0, 4));
                        tblMenuExample.or()
                                .andParentMidEqualTo(tblMenu.getMenuId());
                        List<TblMenu> tblMenuList = tblMenuMapper.selectByExample(tblMenuExample);
                        tblMenuList.add(tblMenu);
                        return tblMenuList.stream();
                    } else {
                        tblMenuExample.clear();
                        tblMenuExample.createCriteria()
                                .andMenuIdEqualTo(tblMenu.getMenuId().substring(0, 4));
                        tblMenuExample.or()
                                .andMenuIdEqualTo(tblMenu.getMenuId().substring(0, 6));
                        List<TblMenu> tblMenuList = tblMenuMapper.selectByExample(tblMenuExample);
                        tblMenuList.add(tblMenu);
                        return tblMenuList.stream();
                    }
                }).filter(distinctByKey(TblMenu::getMenuId))
                .collect(Collectors.toList());
    }

    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
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
