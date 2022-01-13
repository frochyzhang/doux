package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.core.util.result.ResultCodeEnum;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * job group controller
 *
 * @author xuxueli 2016-10-02 20:52:56
 */
@Api(value = "JobGroupController", tags = {"执行器管理接口"})
@RestController
@RequestMapping("/groups")
public class JobGroupController {

    @Resource
    public XxlJobInfoDao xxlJobInfoDao;
    @Resource
    public XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobRegistryDao xxlJobRegistryDao;
    @Value("${xxl.job.jobGroup.appNameRegex}")
    private String appNameRegex;
    @Value("${xxl.job.jobGroup.titleRegex}")
    private String titleRegex;

    @GetMapping
    @ApiOperation("分页查询执行器列表")
    public Result pageList(@RequestParam(name = "current") Integer pageNo,
                           @RequestParam(name = "pageSize") Integer pageSize,
                           @RequestParam(name = "appName", required = false) String appName,
                           @RequestParam(name = "title", required = false) String title) {

        // page query
        List<XxlJobGroup> list = xxlJobGroupDao.pageList((pageNo - 1) * pageSize, pageSize, appName, title);
        return Result.success(new PageInfo<>(list));
    }

    @PostMapping
    @ApiOperation("新增执行器信息")
    public Result save(@RequestBody XxlJobGroup xxlJobGroup) {
        // valid
        if (StringUtils.isBlank(xxlJobGroup.getAppName()) || StringUtils.isBlank(xxlJobGroup.getTitle())) {
            return Result.failure(ResultCodeEnum.PARAM_IS_BLANK);
        }
        if (!xxlJobGroup.getAppName().matches(appNameRegex)) {
            return Result.failure(XxlJobResultCodeEnum.APP_NAME_INVALID);
        }

        if (!xxlJobGroup.getTitle().matches(titleRegex)) {
            return Result.failure(XxlJobResultCodeEnum.TITLE_IS_INVALID);
        }
        //手动录入
        if (xxlJobGroup.getAddressType() != 0) {
            if (StringUtils.isBlank(xxlJobGroup.getAddressList())) {
                return Result.failure(ResultCodeEnum.PARAM_IS_INVALID);
            }

            String[] addressArray = xxlJobGroup.getAddressList().split(",");
            for (String address : addressArray) {
                if (StringUtils.isBlank(address)) {
                    return Result.failure(XxlJobResultCodeEnum.ADDRESS_LIST_INVALID);
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        xxlJobGroupDao.save(xxlJobGroup);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("更新执行器信息")
    public Result update(XxlJobGroup xxlJobGroup) {
        // valid
        if (StringUtils.isBlank(xxlJobGroup.getAppName()) || StringUtils.isBlank(xxlJobGroup.getTitle())) {
            return Result.failure(ResultCodeEnum.PARAM_IS_BLANK);
        }

        if (!xxlJobGroup.getAppName().matches(appNameRegex)) {
            return Result.failure(XxlJobResultCodeEnum.APP_NAME_INVALID);
        }

        if (!xxlJobGroup.getTitle().matches(titleRegex)) {
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
                return Result.failure(ResultCodeEnum.PARAM_IS_BLANK);
            }
            String[] addressArray = xxlJobGroup.getAddressList().split(",");
            for (String address : addressArray) {
                if (StringUtils.isBlank(address)) {
                    return Result.failure(XxlJobResultCodeEnum.ADDRESS_LIST_INVALID);
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        xxlJobGroupDao.update(xxlJobGroup);
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
        // valid
        int count = xxlJobInfoDao.pageListCount(0, 10, groupId, -1, null, null, null);
        if (count > 0) {
            return Result.failure(XxlJobResultCodeEnum.JOB_INFO_NOT_EMPTY);
        }

        List<XxlJobGroup> allList = xxlJobGroupDao.findAll();
        if (allList.size() == 1) {
            return Result.failure(XxlJobResultCodeEnum.JOB_GROUP_ONLY_ONE);
        }

        xxlJobGroupDao.remove(groupId);
        return Result.success();
    }

    @GetMapping("{groupId}")
    @ApiOperation("根据执行器id查询执行器信息")
    public Result loadById(@PathVariable int groupId) {
        XxlJobGroup jobGroup = xxlJobGroupDao.load(groupId);
        return Result.success(jobGroup);
    }
}
