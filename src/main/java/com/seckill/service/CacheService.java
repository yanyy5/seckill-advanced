package com.seckill.service;

public interface CacheService {
    // set
    void setCommonCache(String key, Object value);

    // get
    Object getFromCommonCache(String key);
}
