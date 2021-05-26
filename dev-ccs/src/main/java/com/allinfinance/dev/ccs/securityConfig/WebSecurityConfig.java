package com.allinfinance.dev.ccs.securityConfig;


import com.allinfinance.dev.ccs.securityConfig.handler.*;
import com.allinfinance.dev.ccs.securityConfig.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //登录成功处理逻辑
    @Autowired
    AosAuthenticationSuccessHandler authenticationSuccessHandler;
 
    //登录失败处理逻辑
    @Autowired
    AosAuthenticationFailureHandler authenticationFailureHandler;
 
    //权限拒绝处理逻辑
    @Autowired
    AosAccessDeniedHandler accessDeniedHandler;
 
    //匿名用户访问无权限资源时的异常
    @Autowired
    AosAuthenticationEntryPoint authenticationEntryPoint;

    //登出成功处理逻辑
    @Autowired
    AosLogoutSuccessHandler logoutSuccessHandler;
 
    //访问决策管理器
    @Autowired
    AosAccessDecisionManager accessDecisionManager;
    @Autowired
    JwtAuthenticationTokenFilter authenticationTokenFilter;
    //实现权限拦截
    @Autowired
    AosFilterInvocationSecurityMetadataSource securityMetadataSource;
 
    @Autowired
    private AosAbstractSecurityInterceptor securityInterceptor;
 
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        //获取用户账号密码及权限信息
        return new UserDetailsServiceImpl();
    }
 
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式（强hash方式加密）
        return new BCryptPasswordEncoder();
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests().
                antMatchers("/api/login/account","login/reLogin").permitAll().
                //antMatchers("/**").fullyAuthenticated().
                withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(securityMetadataSource);//安全元数据源
                        o.setAccessDecisionManager(accessDecisionManager);//决策管理器
                        return o;
                    }
                }).
                //登入
                and().formLogin().
                    loginProcessingUrl("/login/account").
                    passwordParameter("userPass").
                    usernameParameter("userName").
                    permitAll().//允许所有用户
                    successHandler(authenticationSuccessHandler).//登录成功处理逻辑
                    failureHandler(authenticationFailureHandler).//登录失败处理逻辑
                and().logout().
                logoutUrl("/login/logout").
                permitAll().//允许所有用户
                logoutSuccessHandler(logoutSuccessHandler).//登出成功处理逻辑
                //异常处理(权限拒绝、登录失效等)
                and().exceptionHandling().
                    accessDeniedHandler(accessDeniedHandler).//权限拒绝处理逻辑
                    authenticationEntryPoint(authenticationEntryPoint).//匿名用户访问无权限资源时的异常处理
                //关闭session
                and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }
}