package com.allinfinance.dev.ccs.dal.service.impl;

import com.allinfinance.dev.ccs.dal.converter.ResponseMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRoleAuthMapper;
import com.allinfinance.dev.ccs.dal.mapper.TblRoleMapper;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthExample;
import com.allinfinance.dev.ccs.dal.model.TblRoleAuthKey;
import com.allinfinance.dev.ccs.dal.model.TblRoleExample;
import com.allinfinance.dev.ccs.dal.paramvo.RoleReqParam;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.allinfinance.dev.ccs.dto.PageableRolesQueryResponseDTO;
import com.allinfinance.dev.ccs.dto.RolesQueryResponseDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @project: dev-parent
 * @description: 角色持久层服务
 * @author: Lum Wang
 * @create: 2021-05-13 16:16
 */
@Service
public class TblRoleServiceImpl implements TblRoleService {

    @Autowired
    private TblRoleMapper tblRoleMapper;

    @Autowired
    private TblRoleAuthMapper tblRoleAuthMapper;

    @Override
    public long countByExample(TblRoleExample example) {
        return tblRoleMapper.countByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(String roleId) {
        return tblRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int insert(TblRole record) {
        return tblRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(TblRole record) {
        return tblRoleMapper.insertSelective(record);
    }

    @Override
    public List<TblRole> selectByExample(TblRoleExample example) {
        return tblRoleMapper.selectByExample(example);
    }

    @Override
    public TblRole selectByPrimaryKey(String roleId) {
        return tblRoleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public int updateByExampleSelective(TblRole record, TblRoleExample example) {
        return tblRoleMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(TblRole record, TblRoleExample example) {
        return tblRoleMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(TblRole record) {
        return tblRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TblRole record) {
        return tblRoleMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<PageableRolesQueryResponseDTO> pageSelectRoles(RoleReqParam roleReqParam) {
        PageHelper.startPage(roleReqParam.getCurrent(), roleReqParam.getPageSize());
        TblRoleExample tblRoleExample = new TblRoleExample();
        TblRoleExample.Criteria criteria = tblRoleExample.createCriteria();
        if (StringUtils.isNotBlank(roleReqParam.getRoleName())) {
            criteria.andRoleNameLike("%" + roleReqParam.getRoleName() + "%");
        }
        if (StringUtils.isNotBlank(roleReqParam.getOrg())) {
            criteria.andOrgEqualTo(roleReqParam.getOrg());
        }
        if (StringUtils.isNotBlank(roleReqParam.getIsAvailable())) {
            criteria.andIsAvailableEqualTo(roleReqParam.getIsAvailable());
        }
        criteria.andWeightLessThan(roleReqParam.getWeight());
        List<PageableRolesQueryResponseDTO> pageableRolesQueryResponseDTOList = tblRoleMapper.selectByExample(tblRoleExample)
                .stream()
                .map(tblRole -> {
                    TblRoleAuthExample tblRoleAuthExample = new TblRoleAuthExample();
                    tblRoleAuthExample.createCriteria()
                            .andRoleIdEqualTo(tblRole.getRoleId());
                    String authId = tblRoleAuthMapper.selectByExample(tblRoleAuthExample)
                            .stream()
                            .map(TblRoleAuthKey::getAuthId)
                            .collect(Collectors.toList()).stream().findFirst().orElse("");
                    return ResponseMapper.INSTANCE.convertToPageableRolesQueryResponseDTO(tblRole, authId);
                }).collect(Collectors.toList());
        return new PageInfo<>(pageableRolesQueryResponseDTOList);
    }

    @Override
    public List<RolesQueryResponseDTO> queryRoles(RoleReqParam roleReqParam) {
        TblRoleExample tblRoleExample = new TblRoleExample();
        TblRoleExample.Criteria criteria = tblRoleExample.createCriteria();
        if (StringUtils.isNotBlank(roleReqParam.getRoleName())) {
            criteria.andRoleNameLike("%" + roleReqParam.getRoleName() + "%");
        }
        if (StringUtils.isNotBlank(roleReqParam.getOrg())) {
            criteria.andOrgEqualTo(roleReqParam.getOrg());
        }
        if (StringUtils.isNotBlank(roleReqParam.getIsAvailable())) {
            criteria.andIsAvailableEqualTo(roleReqParam.getIsAvailable());
        }
        criteria.andWeightLessThan(roleReqParam.getWeight());
        return tblRoleMapper.selectByExample(tblRoleExample)
                .stream()
                .map(ResponseMapper.INSTANCE::convertToRolesQueryResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TblRole selectByRoleName(String roleName) {
        TblRoleExample tblRoleExample = new TblRoleExample();
        tblRoleExample.createCriteria()
                .andRoleNameEqualTo(roleName);
        return tblRoleMapper.selectByExample(tblRoleExample)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
