package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.dal.model.TblMenu;
import com.allinfinance.dev.ccs.dal.model.TblUser;
import com.allinfinance.dev.ccs.dal.paramvo.LoginParam;
import com.allinfinance.dev.ccs.dal.paramvo.MenusReqParam;
import com.allinfinance.dev.ccs.dal.paramvo.SecondCheckPassVo;
import com.allinfinance.dev.ccs.dal.service.TblMenuService;
import com.allinfinance.dev.ccs.dal.service.TblUserService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author liuqi
 * @since 2021-05-14
 */
@RestController
@RequestMapping("/")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    TblUserService userService;
    @RequestMapping(path = "login/reLogin" ,method = RequestMethod.POST)
    @ResponseBody
    public Result getMenusList(@RequestBody SecondCheckPassVo checkPassVo){
        logger.info("接受到的参数:userName-->{},checkCode-->{}", checkPassVo.getUserName(), checkPassVo.getCheckCode());
        if(!"000000".equals(checkPassVo.getCheckCode())){
            return Result.failure(ResultCodeEnum.USER_LOGIN_ERROR);
        }
        return Result.success("");
    }


    @RequestMapping(path = "currentUser" ,method = RequestMethod.GET)
    @ResponseBody
    public Result getCurrentUser(HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtUtil.getUsername(token);
        logger.info("获取当前用户信息:userName-->{}", username);
        TblUser currentUser = userService.selectCurrentUser(username);
        logger.info("获取当前用户信息:currentUser-->{}", currentUser.toString());
        return Result.success(currentUser);
    }
    }

