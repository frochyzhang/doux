package com.allinfinance.dev.common.util.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author qipeng
 * @date 2022/8/25 15:26
 * @desc caffeine的同步加载方式
 */
public class SyncCache<K, V> {
    private final Cache<K, V> cache;

    public SyncCache() {
        this.cache = Caffeine.newBuilder().build();
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.getIfPresent(key);
    }

    public V getOrDefault(K key, V defaultValue) {
        V ret = get(key);
        if (ObjectUtils.isEmpty(ret)) {
            return defaultValue;
        }
        return ret;
    }

    public Boolean containsKey(K key) {
        return cache.getIfPresent(key) != null;
    }

    public V remove(K key) {
        V value = cache.getIfPresent(key);
        cache.invalidate(key);
        return value;
    }

    public List<V> values() {
        List<V> result = new ArrayList<>();
        cache.getAllPresent(result);
        return result;
    }

    public Set<K> keySet() {
        return cache.asMap().keySet();
    }
}
