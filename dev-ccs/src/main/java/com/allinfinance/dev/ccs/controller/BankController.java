package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.BankReqParam;
import com.allinfinance.dev.ccs.dal.service.TblBankService;

import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Bankor ：Lucas Li
 * @date ：2021/5/18 10:50
 * @description：银行
 */
@RestController
@RequestMapping("/platform/banks")
public class BankController {

    private static final Logger logger = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private TblBankService tblBankService;

    //分页查询银行
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Result selectBanks(BankReqParam bankReqParam, HttpServletRequest request) {
        logger.info("BankReqParam: {}", bankReqParam);
        String token = request.getHeader("token");
        String org = JwtUtil.getOrg(token);
        logger.info("获取当前操作用户的机构号:org-->{}", org);
        if (org != null && org.length() != 0 ) {
            if (org.equals("000000000000")) {
                bankReqParam.setOrg(null);
            }
        }
        if (bankReqParam.getCurrent() == null || bankReqParam.getPageSize() == null) {
            bankReqParam.setCurrent(1);
            bankReqParam.setPageSize(10);
        }
        PageInfo<TblBankManage> banks;
        try {
            banks = tblBankService.pageSelectBanks(bankReqParam);
        } catch (Exception e) {
            logger.error("查询银行列表异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }

        logger.debug("查询到的银行列表: {}", banks);
        return Result.success(banks);
    }

    //更新银行
    @RequestMapping(path = "/{BankId}", method = RequestMethod.PUT)
    @ResponseBody
    public Result modifyUser(@RequestBody BankReqParam tblBank, @PathVariable("BankId") String BankId) {
        logger.debug("接收到的请求参数: {},BankId:{}", tblBank, BankId);
        tblBank.setBankId(BankId);
        int result;
        try {
            result = tblBankService.updateByPrimaryKey(tblBank);
        } catch (Exception e) {
            logger.error("更新银行发生异常", e);
            return Result.failure();
        }
        logger.info("result: {}", result);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }


    /**
     * 更新银行信息
     *
     * @param bankReqParam
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public Result updateUserInfo(@RequestBody BankReqParam bankReqParam) {
        logger.info("接收到的更新银行信息: {}", bankReqParam);
        int result = 0;
        try {
            result = tblBankService.updateByPrimaryKeySelective(bankReqParam);
        } catch (Exception e) {
            logger.error("更新银行异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("更新银行执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(result);
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Result addBank(@RequestBody BankReqParam bankReqParam, HttpServletRequest request) {
        logger.info("接收到的新增银行信息: {}", bankReqParam);
        //系统银行重名检查
        TblBankManage tblBank = tblBankService.selectByBankInfo(bankReqParam);
        if (tblBank != null) {
            return Result.failure("该银行已存在", ResultCodeEnum.USER_HAS_EXISTED.code());
        }
        int result = 0;
        try {
            result = tblBankService.insertSelective(bankReqParam);
        } catch (Exception e) {
            logger.error("新增银行异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("新增银行执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(result);
    }

}
