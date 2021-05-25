package com.allinfinance.dev.ccs.securityConfig.handler;


import com.allinfinance.dev.ccs.dal.model.TblPermissionInfo;
import com.allinfinance.dev.ccs.dal.service.TblPermissionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Author: liuqi
 * @Description:
 * @Date Create in 2021/5/15 21:06
 */
@Component
public class AosFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private TblPermissionInfoService tblPermissionService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();

        //查询具体某个接口的权限
          if(requestUrl.contains("?")){
            String[] urlparam = requestUrl.split("\\?");
            requestUrl=urlparam[0];
        }
        TblPermissionInfo permission=tblPermissionService.getPromissionInfo(requestUrl);
        if(permission == null){
            //请求路径没有配置权限，表明该请求接口可以任意访问
            return null;
        }
        String[] attributes = new String[]{permission.getPermissioncode()};
        return SecurityConfig.createList(attributes);
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
