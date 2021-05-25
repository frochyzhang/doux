package com.allinfinance.dev.ccs.securityConfig.service;

import com.allinfinance.dev.ccs.dal.model.TblRolePermissionInfo;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.service.TblRolePermissionInfoService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liuqi
 * @Description:
 * @Date Create in 2021/5/15 14:36
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private TblUserService itbUserService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private TblRolePermissionInfoService itblRolePermissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new RuntimeException("用户名不能为空");
        }
        //根据用户名查询用户
        TblUser user=itbUserService.selectCurrentUser(username);
        //TblUser user=itbUserService.selectCurrentUser("admin");
        System.out.println(passwordEncoder.encode("000000"));
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user != null) {
            //获取该用户所拥有的权限
            List<TblRolePermissionInfo> role_permissionInfos=itblRolePermissionService.getRolePermissionInfByRoleId(user.getRoleId());
                // 声明用户授权
            role_permissionInfos.forEach(permissions -> {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permissions.getPermissioncode());
                    grantedAuthorities.add(grantedAuthority);
                });
        }
            return new User(user.getUserName(), user.getUserPass(), user.getIsAvailable().equals("1")?true:false, user.getNotExpired().equals("1")?true:false,
                user.getCredentialsNotExpired().equals("1")?true:false, user.getAccountNotLocked().equals("1")?true:false, grantedAuthorities);

//        return new User("admin", "000000", true,true,
//               true, true, null);
    }
}
