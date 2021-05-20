package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.BankReqParam;

import com.allinfinance.dev.ccs.dal.service.TblBankService;

import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result selectBanks(BankReqParam bankReqParam){
        logger.info("BankReqParam: {}",bankReqParam);
        if (bankReqParam.getCurrent()==null || bankReqParam.getPageSize()==null){
            bankReqParam.setCurrent(1);
            bankReqParam.setPageSize(10);
        }
        PageInfo<TblBankManage> banks;
        try {
            banks = tblBankService.pageSelectBanks(bankReqParam);
        }catch (Exception e){
            logger.error("查询银行列表异常",e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }

        logger.debug("查询到的银行列表: {}",banks);
        return Result.success(banks);
    }

    //更新银行
    @RequestMapping(path = "/{BankId}",method = RequestMethod.PUT)
    public Result modifyUser(@RequestBody TblBankManage tblBank,@PathVariable("BankId") int BankId){
        logger.debug("接收到的请求参数: {},BankId:{}",tblBank,BankId);
        tblBank.setBankId(BankId);
        int result;
        try {
            result = tblBankService.updateByPrimaryKey(tblBank);
        }catch (Exception e){
            logger.error("更新银行发生异常",e);
            return Result.failure();
        }
        logger.info("result: {}",result);
        if (result == 1){
            return Result.success();
        }else {
            return Result.failure();
        }
    }

    //新增银行
    @RequestMapping(method = RequestMethod.POST)
    public Result createBank(@RequestBody TblBankManage tblBank){
        logger.info("将新增的银行: {}",tblBank);
        int result;
        try {
            result = tblBankService.insertSelective(tblBank);
        }catch (Exception e){
            logger.error("新增银行发生异常",e);
            return Result.failure();
        }
        logger.info("新增结果: {}",result);
        if (result == 1){
            return Result.success();
        }else {
            return Result.failure();
        }
    }

}
