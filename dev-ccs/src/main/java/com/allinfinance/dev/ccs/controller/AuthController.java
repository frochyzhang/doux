package com.allinfinance.dev.ccs.controller;

import com.allinfinance.dev.ccs.dal.model.TblAuth;
import com.allinfinance.dev.ccs.dal.model.TblRole;
import com.allinfinance.dev.ccs.dal.service.TblAuthService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @project: dev-parent
 * @description: 权限管理
 * @author: Lum Wang
 * @create: 2021-05-14 10:52
 */
@RestController
@RequestMapping("/platform")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private TblAuthService tblAuthService;

    //分页查询权限
    @RequestMapping(path = "/auths",method = RequestMethod.GET)
    public PageInfo<TblAuth> selectUsers(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize){
        logger.info("接受到的参数:pageNo-{},pageSize-{}",pageNo,pageSize);
        PageInfo<TblAuth> auths = tblAuthService.pageSelectAuths(pageNo,pageSize);
        logger.info("查询到的权限列表: {}",auths);
        return auths;
    }

    //更新权限
    @RequestMapping(path = "/auths/{authId}",method = RequestMethod.PUT)
    public boolean modifyUser(@RequestBody TblAuth tblAuth,@PathVariable("authId") int authId){
        logger.info("接收到的请求参数: {},authId:{}",tblAuth,authId);
        tblAuth.setAuthId(authId);

        int result = tblAuthService.updateByPrimaryKey(tblAuth);

        logger.info("result: {}",result);
        return result == 1;
    }

    //新增权限
    @RequestMapping(path = "/auths",method = RequestMethod.POST)
    public boolean createAuth(@RequestBody TblAuth tblAuth){
        logger.info("将新增的权限: {}",tblAuth);
        int result = tblAuthService.insertSelective(tblAuth);
        logger.info("新增结果: {}",result);
        return result == 1;
    }

}
