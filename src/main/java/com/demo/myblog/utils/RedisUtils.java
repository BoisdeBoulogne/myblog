package com.demo.myblog.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    public   <T> void  set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }
    public <T> T get (String key){
        return (T)redisTemplate.opsForValue().get(key);
    }

    public <T> void set(String key, T value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }
    public Integer getKeyCountByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");

        return keys != null ? keys.size() : 0;
    }
}
