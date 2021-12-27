package com.allinfinance.dev.xxl.job.admin.controller;

import com.allinfinance.dev.core.util.result.Result;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobGroup;
import com.allinfinance.dev.xxl.job.admin.core.model.XxlJobRegistry;
import com.allinfinance.dev.xxl.job.admin.core.util.I18nUtil;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobGroupDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobInfoDao;
import com.allinfinance.dev.xxl.job.admin.dao.XxlJobRegistryDao;
import com.xxl.job.core.enums.RegistryConfig;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * job group controller
 *
 * @author xuxueli 2016-10-02 20:52:56
 */
@RestController
@RequestMapping("/groups")
public class JobGroupController {

    @Resource
    public XxlJobInfoDao xxlJobInfoDao;
    @Resource
    public XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobRegistryDao xxlJobRegistryDao;

    @GetMapping
    public Result pageList(@RequestParam(required = false, defaultValue = "0") int start,
                           @RequestParam(required = false, defaultValue = "10") int length,
                           String appName, String title) {

        // page query
        List<XxlJobGroup> list = xxlJobGroupDao.pageList(start, length, appName, title);
        int listCount = xxlJobGroupDao.pageListCount(start, length, appName, title);

        // package result
        Map<String, Object> maps = new HashMap<>();
        maps.put("recordsTotal", listCount);        // 总记录数
        maps.put("recordsFiltered", listCount);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return Result.success(maps);
    }

    @PostMapping
    public Result save(XxlJobGroup xxlJobGroup) {

        // valid
        if (xxlJobGroup.getAppname() == null || xxlJobGroup.getAppname().trim().length() == 0) {
            return Result.failure("500", (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
            return Result.failure("500", I18nUtil.getString("jobgroup_field_appname_length"));
        }
        if (xxlJobGroup.getAppname().contains(">") || xxlJobGroup.getAppname().contains("<")) {
            return Result.failure("500", "AppName" + I18nUtil.getString("system_unvalid"));
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().length() == 0) {
            return Result.failure("500", (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (xxlJobGroup.getTitle().contains(">") || xxlJobGroup.getTitle().contains("<")) {
            return Result.failure("500", I18nUtil.getString("jobgroup_field_title") + I18nUtil.getString("system_unvalid"));
        }
        if (xxlJobGroup.getAddressType() != 0) {
            if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().length() == 0) {
                return Result.failure("500", I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            if (xxlJobGroup.getAddressList().contains(">") || xxlJobGroup.getAddressList().contains("<")) {
                return Result.failure("500", I18nUtil.getString("jobgroup_field_registryList") + I18nUtil.getString("system_unvalid"));
            }

            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    return Result.failure("500", I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        int ret = xxlJobGroupDao.save(xxlJobGroup);
        return Result.success();
    }

    @PutMapping
    public Result update(XxlJobGroup xxlJobGroup) {
        // valid
        if (xxlJobGroup.getAppname() == null || xxlJobGroup.getAppname().trim().length() == 0) {
            return Result.failure("500", (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
            return Result.failure("500", I18nUtil.getString("jobgroup_field_appname_length"));
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().length() == 0) {
            return Result.failure("500", (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (xxlJobGroup.getAddressType() == 0) {
            // 0=自动注册
            List<String> registryList = findRegistryByAppName(xxlJobGroup.getAppname());
            StringBuilder addressListStr = null;
            if (registryList != null && !registryList.isEmpty()) {
                Collections.sort(registryList);
                addressListStr = new StringBuilder();
                for (String item : registryList) {
                    addressListStr.append(item).append(",");
                }
                addressListStr = new StringBuilder(addressListStr.substring(0, addressListStr.length() - 1));
            }
            xxlJobGroup.setAddressList(addressListStr.toString());
        } else {
            // 1=手动录入
            if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().length() == 0) {
                return Result.failure("500", I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    return Result.failure("500", I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        int ret = xxlJobGroupDao.update(xxlJobGroup);
        return Result.success();
    }

    private List<String> findRegistryByAppName(String appnameParam) {
        HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
        List<XxlJobRegistry> list = xxlJobRegistryDao.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
        if (list != null) {
            for (XxlJobRegistry item : list) {
                if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                    String appname = item.getRegistryKey();
                    List<String> registryList = appAddressMap.get(appname);
                    if (registryList == null) {
                        registryList = new ArrayList<String>();
                    }

                    if (!registryList.contains(item.getRegistryValue())) {
                        registryList.add(item.getRegistryValue());
                    }
                    appAddressMap.put(appname, registryList);
                }
            }
        }
        return appAddressMap.get(appnameParam);
    }

    @DeleteMapping("{groupId}")
    public Result remove(@PathVariable int groupId) {

        // valid
        int count = xxlJobInfoDao.pageListCount(0, 10, groupId, -1, null, null, null);
        if (count > 0) {
            return Result.failure("500", I18nUtil.getString("jobgroup_del_limit_0"));
        }

        List<XxlJobGroup> allList = xxlJobGroupDao.findAll();
        if (allList.size() == 1) {
            return Result.failure("500", I18nUtil.getString("jobgroup_del_limit_1"));
        }

        int ret = xxlJobGroupDao.remove(groupId);
        return Result.success();
    }

    @GetMapping("{groupId}")
    public Result loadById(@PathVariable int groupId) {
        XxlJobGroup jobGroup = xxlJobGroupDao.load(groupId);
        return Result.success(jobGroup);
    }

}
