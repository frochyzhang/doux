package com.allinfinance.dev.ccs.securityConfig.service;

import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.UserReqParam;
import com.allinfinance.dev.ccs.dal.service.TblRolePermissionInfoService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String org = request.getParameter("org");
        if (username == null || "".equals(username)) {
            throw new InternalAuthenticationServiceException("用户名不能为空");
        }
        if(StringUtils.isBlank(org)){
            throw new RuntimeException("机构号不能为空");
        }
        //根据用户名查询用户
        UserReqParam reqParam = new UserReqParam();
        reqParam.setOrg(org);
        reqParam.setUserName(username);
        TblUser user=itbUserService.selectByNameAndOrg(reqParam);
        if (user == null) {
            throw new InternalAuthenticationServiceException("用户名不存在");
        }
        if(AosContent.ACCOUNT_DELETE.equals(user.getIsAvailable())){
            throw new DisabledException("账户已被删除！");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user != null) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+user.getRoleId());
            grantedAuthorities.add(grantedAuthority);

        }
            return new User(user.getUserName(), user.getUserPass(), user.getIsAvailable().equals("1")?true:false, user.getNotExpired().equals("1")?true:false,
                user.getCredentialsNotExpired().equals("1")?true:false, user.getAccountNotLocked().equals("1")?true:false, grantedAuthorities);
    }
}
