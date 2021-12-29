package com.allinfinance.dev.ccs.controller;


import com.allinfinance.dev.ccs.content.AosContent;
import com.allinfinance.dev.ccs.dal.model.TblBankManage;
import com.allinfinance.dev.ccs.dal.paramvo.BankManageReqParam;
import com.allinfinance.dev.ccs.dal.service.TblBankManageService;
import com.allinfinance.dev.ccs.security.handler.util.JwtUtil;
import com.allinfinance.dev.ccs.utils.IdUtils;
import com.allinfinance.dev.ccs.utils.annotation.OperLog;
import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Bankor ：Li
 * @date ：2021/5/18 10:50
 * @description：银行
 */
@RestController
@RequestMapping("/platform/banks")
public class BankManageController {

    private static final Logger logger = LoggerFactory.getLogger(BankManageController.class);

    @Autowired
    private TblBankManageService tblBankManageService;

    //查询所有银行列表 无分页
    @GetMapping(path = "/all")
    @OperLog(operModul = "银行管理-银行列表", operType = AosContent.QUERY, operDesc = "无分页查询所有银行列表")
    public Result selectAllBanks(BankManageReqParam bankManageReqParam, HttpServletRequest request) {
        logger.info("查询所有银行列表参数: {}", bankManageReqParam);
        // 判断当前操作人员等级 是否为超级管理员
        String token = request.getHeader(AosContent.AOS_TOKEN);
        // 不是超级管理员只能查询本机构的信息
        if (!AosContent.ROLE_WEIGHT_SUPER_ADMIN.equals(JwtUtil.getWeight(token))) {
            bankManageReqParam.setOrg(JwtUtil.getOrg(token));
        }
        List<TblBankManage> tblBankManages = tblBankManageService.selectBankInfo(bankManageReqParam);
        return Result.success(tblBankManages);
    }

    //分页查询银行
    @GetMapping
    @OperLog(operModul = "银行管理-银行列表", operType = AosContent.QUERY, operDesc = "分页查询银行列表")
    public Result selectPageBanks(BankManageReqParam bankManageReqParam, HttpServletRequest request) {
        logger.info("分页查询银行列表参数: {}", bankManageReqParam);
        String token = request.getHeader(AosContent.AOS_TOKEN);
        // 不是超级管理员只能查询本机构的信息，添加机构号查询条件作为限制
        if (!AosContent.ROLE_WEIGHT_SUPER_ADMIN.equals(JwtUtil.getWeight(token))) {
            bankManageReqParam.setOrg(JwtUtil.getOrg(token));
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
    @PutMapping(path = "/{BankId}")
    @OperLog(operModul = "银行管理-更新银行", operType = AosContent.UPDATE, operDesc = "根据id更新银行信息")
    public Result modifyUser(@RequestBody BankManageReqParam bankManageReqParam, @PathVariable("BankId") String bankId, HttpServletRequest request) {
        logger.debug("更新操作接收到的请求参数: {},bankId:{}", bankManageReqParam, bankId);
        TblBankManage tblBankManage = new TblBankManage();
        tblBankManage.setBankName(bankManageReqParam.getBankName());
        tblBankManage.setOrg(bankManageReqParam.getOrg());
        tblBankManage.setBankNameEn(bankManageReqParam.getBankNameEn());
        tblBankManage.setIsAvailable(bankManageReqParam.getIsAvailable());
        tblBankManage.setBankId(bankId);
        tblBankManage.setUpdateTime(new Date());
        tblBankManage.setUpdateBy(JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN)));
        try {
            tblBankManageService.updateByPrimaryKey(tblBankManage);
        } catch (Exception e) {
            logger.error("更新银行发生异常", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        return Result.success();
    }


    /**
     * 更新银行信息
     *
     * @param bankManageReqParam
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @OperLog(operModul = "银行管理-更新银行", operType = AosContent.UPDATE, operDesc = "更新银行信息")
    public Result updateBankManage(@RequestBody BankManageReqParam bankManageReqParam, HttpServletRequest request) {
        logger.info("接收到的更新银行信息: {}", bankManageReqParam);
        TblBankManage tblBankManage = new TblBankManage();
        tblBankManage.setBankId(bankManageReqParam.getBankId());
        tblBankManage.setIsAvailable(bankManageReqParam.getIsAvailable());
        tblBankManage.setBankNameEn(bankManageReqParam.getBankNameEn());
        tblBankManage.setBankName(bankManageReqParam.getBankName());
        tblBankManage.setOrg(bankManageReqParam.getOrg());
        // 设置更新人和更新时间
        tblBankManage.setUpdateBy(JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN)));
        tblBankManage.setUpdateTime(new Date());
        try {
            tblBankManageService.updateByPrimaryKeySelective(tblBankManage);
        } catch (Exception e) {
            logger.error("更新银行异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("更新银行执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success();
    }

    /**
     * 删除银行信息
     *
     * @param bankManageReqParam
     * @return
     */
    @DeleteMapping
    @OperLog(operModul = "银行管理-删除银行", operType = AosContent.DELETE, operDesc = "删除银行信息")
    public Result delBankManage(@RequestBody BankManageReqParam bankManageReqParam) {
        logger.info("删除的银行信息: {}", bankManageReqParam);
        try {
            tblBankManageService.deleteByPrimaryKey(bankManageReqParam);
        } catch (Exception e) {
            logger.error("删除银行异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("删除银行执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success();
    }

    @PostMapping
    @OperLog(operModul = "银行管理-新增银行", operType = AosContent.INSERT, operDesc = "新增银行信息")
    public Result addBank(@RequestBody BankManageReqParam bankManageReqParam, HttpServletRequest request) {
        logger.info("新增银行信息参数: {}", bankManageReqParam);
        TblBankManage tblBankManage = new TblBankManage();
        tblBankManage.setBankId(IdUtils.getId());
        tblBankManage.setIsAvailable(bankManageReqParam.getIsAvailable());
        tblBankManage.setBankNameEn(bankManageReqParam.getBankNameEn());
        tblBankManage.setBankName(bankManageReqParam.getBankName());
        tblBankManage.setOrg(bankManageReqParam.getOrg());
        // 设置创建和创建时间
        tblBankManage.setCreateBy(JwtUtil.getUsername(request.getHeader(AosContent.AOS_TOKEN)));
        tblBankManage.setCreateTime(new Date());
        //系统银行重名检查
        List<TblBankManage> tblBankManages = tblBankManageService.selectBankInfo(bankManageReqParam);
        if (!tblBankManages.isEmpty()) {
            return Result.failure("该银行已存在", ResultCodeEnum.USER_HAS_EXISTED.code());
        }
        try {
            tblBankManageService.insertSelective(tblBankManage);
        } catch (Exception e) {
            logger.error("新增银行异常!", e);
            return Result.failure(ResultCodeEnum.GENERIC_EXCEPTION);
        }
        logger.info("新增银行执行结果: {}", Result.success(ResultCodeEnum.SUCCESS));
        return Result.success();
    }
}
