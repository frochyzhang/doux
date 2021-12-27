package com.allinfinance.dev.ccs.security.handler;


import com.allinfinance.dev.ccs.dal.model.TblApiPermissionInfo;
import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import com.allinfinance.dev.ccs.dal.service.TblApiPermissionInfoService;
import com.allinfinance.dev.ccs.dal.service.TblRolePermissionInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: liuqi
 * @Description:
 * @Date Create in 2021/5/15 21:06
 */
@Component
public class AosFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private TblApiPermissionInfoService tblPermissionService;
    @Autowired
    private TblRolePermissionInfoService itblRolePermissionService;
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();

         if(StringUtils.equals("/getPublicKey",requestUrl)){
             return null;
         }
        //查询具体某个接口的权限
          if(requestUrl.contains("?")){
            String[] urlparam = requestUrl.split("\\?");
            requestUrl=urlparam[0];
        }
        TblApiPermissionInfo permission=tblPermissionService.getPromissionInfo(requestUrl);
        if(permission == null){
            //请求路径没有配置权限，表明该请求接口可以任意访问
            return null;
        }
        List<TblRolePermissionInfo> roleIdByPermissionCode = itblRolePermissionService.getRoleIdByPermissionCode(permission.getPermissioncode());
        ArrayList<String> arrayList = new ArrayList<>();

        roleIdByPermissionCode.forEach((item)->{
            arrayList.add("ROLE_"+item.getRoleId());
        });
        return SecurityConfig.createList(arrayList.toArray(new String[arrayList.size()]));
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
