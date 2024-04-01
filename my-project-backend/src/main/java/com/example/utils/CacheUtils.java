package com.example.utils;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 */
@Component
public class CacheUtils {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 将对象以JSON字符串的形式放入缓存
     * @param key  key
     * @param data 对象
     * @param expire 过期时间 单位 秒
     */
    public <T> void saveObjectToCache(String key, T data , long expire)
    {
        stringRedisTemplate.opsForValue().set(key, JSONObject.from(data).toJSONString(),expire, TimeUnit.SECONDS);
    }


    /**
     * 将集合以JSON字符串的形式放入缓存
     * @param key  key
     * @param list 集合
     * @param expire 过期时间 单位 秒
     */
    public <T> void saveListToCache(String key, List<T> list , long expire)
    {
        stringRedisTemplate.opsForValue().set(key, JSONArray.from(list).toJSONString(),expire, TimeUnit.SECONDS);
    }


    /**
     * 取对象
     * @param key  对应key值
     * @param dataType 目标类型
     */
    public <T> T takeFromCache(String key, Class<T> dataType)
    {
        String s = stringRedisTemplate.opsForValue().get(key);
        if(s==null) return null;
        return JSONObject.parseObject(s).to(dataType);
    }


    /**
     * 从缓存中取集合
     * @param key   对应key值
     * @param dataType  转换后的目标类型
     */
    public <T> List<T> takeListFromCache(String key, Class<T> dataType)
    {
        String s = stringRedisTemplate.opsForValue().get(key);
        if(s==null) return null;
        return JSONArray.parseArray(s).toList(dataType);
    }

    public void deleteCache(String key)
    {
        stringRedisTemplate.delete(key);
    }

    public void deleteCachePattern(String key)
    {
        Set<String> keys = stringRedisTemplate.keys(key);

       if(keys!=null) stringRedisTemplate.delete(keys);

    }

}
