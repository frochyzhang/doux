package com.allinfinance.dev.hsp.cache;

import com.allinfinance.dev.hsp.model.TblHsmKey;
import com.allinfinance.dev.hsp.service.dal.TblHsmKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通过spring和concurrencyHashMap实现的密钥本地缓存
 *
 * @author zhangkun
 */
@Component
public class KeyCache {

    @Autowired
    private TblHsmKeyService tblHsmKeyService;

    /**
     * 根据密钥索引从缓存中取得密钥，如果缓存中已存在此密钥则直接返回，否则从数据库中取出密钥并返回。
     *
     * @param keyIndex 密钥索引
     * @return 密钥
     */
    @Cacheable(value = "keyCache", key = "#keyIndex")
    public TblHsmKey get(String keyIndex) {
        TblHsmKey hsmKey = tblHsmKeyService.selectByKeyIndex(keyIndex);
        if (hsmKey != null) {
            return hsmKey;
        } else {
            throw new RuntimeException("keyIndex:[" + keyIndex + "],密钥不存在");
        }
    }

    /**
     * 更新或新增密钥
     *
     * @param keyIndex 密钥索引
     * @param keyValue 密钥
     */
    @CacheEvict(value = "keyCache", key = "#keyIndex")
    @Transactional
    public void put(String keyIndex, String keyValue) {
        // 将KEY存入数据库
        TblHsmKey hsmKey = tblHsmKeyService.selectByKeyIndex(keyIndex);
        if (hsmKey == null) {
            hsmKey = new TblHsmKey();
            hsmKey.setKeyIndex(keyIndex);
            hsmKey.setKeyValue(keyValue);
            tblHsmKeyService.insert(hsmKey);
        } else {
            hsmKey.setKeyValue(keyValue);
            tblHsmKeyService.updateByPrimaryKey(hsmKey);
        }
    }

    /**
     * 根据密钥索引从本地缓存中删除密钥，在接收到缓存更新命令时使用
     *
     * @param keyIndex 密钥索引
     */
    @CacheEvict(value = "keyCache", key = "#keyIndex")
    public void remove(String keyIndex) {

    }

    /**
     * 清空本地缓存，在整个本地缓存全部刷新时使用
     */
    @CacheEvict(value = "keyCache", allEntries = true)
    public void clear() {

    }

}
