package com.allinfinance.dev.ccs.securityConfig.service;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.service.TblRoleService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建UserDetails
 * @Author: liuqi
 * @Description:
 * @Date Create in 2021/5/15 14:36
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private TblUserService itbUserService;
    @Autowired
    private TblRoleService roleService;
    @Override
    public UserDetails loadUserByUsername(String username) throws RuntimeException{
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String org = request.getParameter("org");
        if (username == null || "".equals(username)) {
            throw new InternalAuthenticationServiceException("用户名不能为空");
        }

        //根据用户名查询用户
        TblUser user=itbUserService.selectCurrentUser(username);
        if (user == null) {
            throw new InternalAuthenticationServiceException("用户名不存在");
        }
        if(AosContent.IS_AVAILABLE_FALSE.equals(user.getIsAvailable())){
            throw new DisabledException("账户已被删除！");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user != null) {
            TblRole tblRole = roleService.selectByPrimaryKey(user.getRoleId());
            //登录的时候  如果用户对应的角色被删除，给出异常
            if(tblRole!=null){
               if(StringUtils.equals(AosContent.IS_AVAILABLE_FALSE,tblRole.getIsAvailable())){
                 throw new  DisabledException("账户配置的角色已失效！");
               };
            }
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+user.getRoleId());
            grantedAuthorities.add(grantedAuthority);

        }
            return new User(user.getUserName(), user.getUserPass(), user.getIsAvailable().equals(AosContent.IS_AVAILABLE_TRUE)?true:false,
                    user.getNotExpired().equals(AosContent.ACCOUNT_OK)?true:false,
                    user.getCredentialsNotExpired().equals(AosContent.ACCOUNT_OK)?true:false,
                    user.getAccountNotLocked().equals(AosContent.ACCOUNT_OK)?true:false,
                    grantedAuthorities);
    }
}
