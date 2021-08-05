package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.BankManageReqParam;
import com.allinfinance.dev.ccs.dal.service.TblBankManageService;
import com.allinfinance.dev.ccs.result.Result;
import com.allinfinance.dev.ccs.result.ResultCodeEnum;
import com.allinfinance.dev.ccs.securityConfig.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Bankor ：Lucas Li
 * @date ：2021/5/18 10:50
 * @description：银行
 */
@RestController
@RequestMapping("/platform/banks")
public class BankManageController {

    private static final Logger logger = LoggerFactory.getLogger(BankManageController.class);

    @Autowired
    private TblBankManageService tblBankManageService;

    //分页查询银行
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @OperLog(operModul = "银行管理-银行列表", operType = AosContent.QUERY, operDesc = "分页查询银行列表")
    public Result selectBanks(BankManageReqParam bankManageReqParam, HttpServletRequest request) {
        logger.info("bankManageReqParam: {}", bankManageReqParam);
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String org = JwtUtil.getOrg(token);
        logger.info("获取当前操作用户的机构号:org-->{}", org);
        if (org != null && org.length() != 0) {
            if ((!org.equals(AosContent.ALLINFINANCE_ORG)) && bankManageReqParam.getOrg() == null) {
                bankManageReqParam.setOrg(org);
            }
        }
        if (bankManageReqParam.getCurrent() == null || bankManageReqParam.getPageSize() == null) {
            // 如果请求中没有传页码和大小就调用查询所有的银行的方法
            List<TblBankManage> tblBankManages = tblBankManageService.selectByBankInfo(bankManageReqParam);
            return Result.success(tblBankManages);
        }
        PageInfo<TblBankManage> banks;
        try {
            banks = tblBankManageService.pageSelectBanks(bankManageReqParam);
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
    @OperLog(operModul = "银行管理-更新银行", operType = AosContent.UPDATE, operDesc = "根据id更新银行信息")
    public Result modifyUser(@RequestBody BankManageReqParam tblBank, @PathVariable("BankId") String bankId, HttpServletRequest request) {
        logger.debug("更新操作接收到的请求参数: {},bankId:{}", tblBank, bankId);
        String token = request.getHeader(AosContent.AOS_TOKEN);
        String userName = JwtUtil.getUsername(token);
        tblBank.setBankId(bankId);
        tblBank.setUpdateBy(userName);
        int result;
        try {
            result = tblBankManageService.updateByPrimaryKey(tblBank);
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
     * @param bankManageReqParam
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    @OperLog(operModul = "银行管理-更新银行",operType = AosContent.UPDATE,operDesc = "更新银行信息")
    public Result updateBankManage(@RequestBody BankManageReqParam bankManageReqParam) {
        logger.info("接收到的更新银行信息: {}", bankManageReqParam);
        int result = 0;
        try {
            result = tblBankManageService.updateByPrimaryKeySelective(bankManageReqParam);
        } catch (Exception e) {
            logger.error("更新银行异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("更新银行执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(result);
    }

    /**
     * 删除银行信息
     *
     * @param bankManageReqParam
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    @OperLog(operModul = "银行管理-删除银行",operType = AosContent.DELETE,operDesc = "删除银行信息")
    public Result delBankManage(@RequestBody BankManageReqParam bankManageReqParam) {
        logger.info("删除的银行信息: {}", bankManageReqParam);
        int result = 0;
        try {
            result = tblBankManageService.deleteByPrimaryKey(bankManageReqParam);
        } catch (Exception e) {
            logger.error("删除银行异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("删除银行执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(result);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @OperLog(operModul = "银行管理-新增银行",operType = AosContent.INSERT,operDesc = "新增银行信息")
    public Result addBank(@RequestBody BankManageReqParam bankManageReqParam, HttpServletRequest request) {
        logger.info("接收到的新增银行信息: {}", bankManageReqParam);
        String token = request.getHeader( AosContent.AOS_TOKEN);
        String userName = JwtUtil.getUsername(token);
        // 设置创建和创建时间
        bankManageReqParam.setCreateBy(userName);
        bankManageReqParam.setCreateTime(new Date());
        //系统银行重名检查
        List<TblBankManage> tblBankManages = tblBankManageService.selectByBankInfo(bankManageReqParam);
        if (tblBankManages.size() != 0) {
            return Result.failure("该银行已存在", ResultCodeEnum.USER_HAS_EXISTED.code());
        }
        int result = 0;
        try {
            result = tblBankManageService.insertSelective(bankManageReqParam);
        } catch (Exception e) {
            logger.error("新增银行异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("新增银行执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success(result);
    }

}
