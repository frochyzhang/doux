package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.paramvo.UpdatePasswordParam;
import com.allinfinance.dev.ccs.dal.service.TblAccountAervice;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.allinfinance.dev.core.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/account")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    TblAccountAervice accountAervice;

    @RequestMapping(path = "/updateNewPass", method = RequestMethod.POST)
    @ResponseBody
    @OperLog(operModul = "账户管理-更新密码",operType = AosContent.UPDATE,operDesc = "更新账户密码")
    public Result updateNewPass(@RequestBody UpdatePasswordParam passwordParam, HttpServletRequest request) {
        logger.info("************更新账户密码Controller开始*************");
        Result result = accountAervice.updateNewPass(passwordParam);
        logger.debug("更新账户密码返回结果：响应码：[{}]，响应描述：[{}]",result.getCode(),result.getMessage());
        logger.info("************更新账户密码Controller结束*************");
        return result;
    }
}

