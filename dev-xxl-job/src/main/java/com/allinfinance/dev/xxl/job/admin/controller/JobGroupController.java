package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.xxl.job.admin.constant.XxlJobResultCodeEnum;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobGroup;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobGroupDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobInfoDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobRegistryDao;
import com.github.pagehelper.PageInfo;
import com.xxl.job.core.enums.RegistryConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

/**
 * job group controller
 *
 * @author xuxueli 2016-10-02 20:52:56
 */
@Api(value = "JobGroupController", tags = {"执行器管理接口"})
@RestController
@RequestMapping("/job/groups")
public class JobGroupController {
    private static final Logger logger = LoggerFactory.getLogger(JobGroupController.class);

    @Resource
    public XxlJobInfoDao xxlJobInfoDao;
    @Resource
    public XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobRegistryDao xxlJobRegistryDao;
    @Value("${xxl.job.jobGroup.appNameRegex:^[a-z][a-z0-9-]{3,63}$}")
    private String appNameRegex;
    @Value("${xxl.job.jobGroup.titleRegex:^.{4,12}$}")
    private String titleRegex;

    @GetMapping
    @ApiOperation("分页查询执行器列表")
    public Result pageList(@RequestParam(name = "current") Integer pageNo,
                           @RequestParam(name = "pageSize") Integer pageSize,
                           @RequestParam(name = "appName", required = false) String appName,
                           @RequestParam(name = "title", required = false) String title) {
        logger.info("分页查询执行器列表, appName: {}, 执行器名称: {}", appName, title);
        // page query
        List<XxlJobGroup> xxlJobGroupList = xxlJobGroupDao.pageList((pageNo - 1) * pageSize, pageSize, appName, title);
        int pageListCount = xxlJobGroupDao.pageListCount(appName, title);
        PageInfo<XxlJobGroup> xxlJobGroupPageInfo = new PageInfo<>(xxlJobGroupList);
        xxlJobGroupPageInfo.setTotal(pageListCount);
        logger.info("分页查询执行器列表完成");
        logger.debug("执行器列表: {}", xxlJobGroupList);
        return Result.success(xxlJobGroupPageInfo);
    }

    @PostMapping
    @ApiOperation("新增执行器信息")
    public Result save(@RequestBody @Valid XxlJobGroup xxlJobGroup) {
        logger.info("新增执行器信息: {}", xxlJobGroup);
        // valid
        if (!xxlJobGroup.getAppName().matches(appNameRegex)) {
            logger.error("appName不符合格式要求");
            return Result.failure(XxlJobResultCodeEnum.APP_NAME_INVALID);
        }

        if (!xxlJobGroup.getTitle().matches(titleRegex)) {
            logger.error("执行器名称不符合格式要求");
            return Result.failure(XxlJobResultCodeEnum.TITLE_IS_INVALID);
        }
        //手动录入
        if (xxlJobGroup.getAddressType() != 0) {
            if (StringUtils.isBlank(xxlJobGroup.getAddressList())) {
                logger.error("执行器手动录入时机器地址列表为空");
                return Result.failure(XxlJobResultCodeEnum.ADDRESS_LIST_EMPTY);
            }

            String[] addressArray = xxlJobGroup.getAddressList().split(",");
            for (String address : addressArray) {
                if (StringUtils.isBlank(address)) {
                    logger.error("执行器手动录入时机器地址列表格式不正确");
                    return Result.failure(XxlJobResultCodeEnum.ADDRESS_LIST_INVALID);
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        xxlJobGroupDao.save(xxlJobGroup);
        logger.info("新增执行器信息完成");
        return Result.success();
    }

    @PutMapping
    @ApiOperation("更新执行器信息")
    public Result update(@RequestBody @Valid XxlJobGroup xxlJobGroup) {
        logger.info("更新执行器信息: {}", xxlJobGroup);
        // valid
        if (!xxlJobGroup.getAppName().matches(appNameRegex)) {
            logger.error("appName不符合格式要求");
            return Result.failure(XxlJobResultCodeEnum.APP_NAME_INVALID);
        }

        if (!xxlJobGroup.getTitle().matches(titleRegex)) {
            logger.error("执行器名称不符合格式要求");
            return Result.failure(XxlJobResultCodeEnum.TITLE_IS_INVALID);
        }
        if (xxlJobGroup.getAddressType() == 0) {
            // 0=自动注册
            List<String> registryList = findRegistryByAppName(xxlJobGroup.getAppName());
            StringBuilder addressListStr = new StringBuilder();
            if (CollectionUtils.isNotEmpty(registryList)) {
                Collections.sort(registryList);
                for (String item : registryList) {
                    addressListStr.append(item).append(",");
                }
                addressListStr.deleteCharAt(addressListStr.lastIndexOf(","));
            }
            xxlJobGroup.setAddressList(addressListStr.toString());
        } else {
            // 1=手动录入
            if (StringUtils.isBlank(xxlJobGroup.getAddressList())) {
                logger.error("执行器手动录入时机器地址列表为空");
                return Result.failure(XxlJobResultCodeEnum.ADDRESS_LIST_EMPTY);
            }
            String[] addressArray = xxlJobGroup.getAddressList().split(",");
            for (String address : addressArray) {
                if (StringUtils.isBlank(address)) {
                    logger.error("执行器手动录入时机器地址列表格式不正确");
                    return Result.failure(XxlJobResultCodeEnum.ADDRESS_LIST_INVALID);
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        xxlJobGroupDao.update(xxlJobGroup);
        logger.info("更新执行器信息完成");
        return Result.success();
    }

    private List<String> findRegistryByAppName(String appName) {
        HashMap<String, List<String>> appAddressMap = new HashMap<>();
        xxlJobRegistryDao.findAll(RegistryConfig.DEAD_TIMEOUT, new Date())
                .forEach(xxlJobRegistry -> {
                    if (RegistryConfig.RegistType.EXECUTOR.name().equals(xxlJobRegistry.getRegistryGroup())) {
                        String registryKey = xxlJobRegistry.getRegistryKey();
                        List<String> registryList = appAddressMap.get(registryKey);
                        if (registryList == null) {
                            registryList = new ArrayList<>();
                        }

                        if (!registryList.contains(xxlJobRegistry.getRegistryValue())) {
                            registryList.add(xxlJobRegistry.getRegistryValue());
                        }
                        appAddressMap.put(registryKey, registryList);
                    }
                });
        return appAddressMap.get(appName);
    }

    @DeleteMapping("{groupId}")
    @ApiOperation("删除执行器")
    public Result remove(@PathVariable int groupId) {
        logger.info("删除执行器信息, jobGroupId: {}", groupId);
        // valid
        int count = xxlJobInfoDao.pageListCount(groupId, null, null, null, null);
        if (count > 0) {
            logger.error("执行器有绑定的任务存在，无法删除");
            return Result.failure(XxlJobResultCodeEnum.JOB_INFO_NOT_EMPTY);
        }

        xxlJobGroupDao.remove(groupId);
        logger.info("删除执行器信息完成");
        return Result.success();
    }

    @GetMapping("{groupId}")
    @ApiOperation("根据执行器id查询执行器信息")
    public Result loadById(@PathVariable int groupId) {
        logger.info("根据执行器id查询执行器信息, jobGroupId: {}", groupId);
        XxlJobGroup jobGroup = xxlJobGroupDao.load(groupId);
        logger.debug("执行器信息: {}", jobGroup);
        return Result.success(jobGroup);
    }
}
