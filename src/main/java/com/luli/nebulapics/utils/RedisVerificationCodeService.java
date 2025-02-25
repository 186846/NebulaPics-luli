package com.luli.nebulapics.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisVerificationCodeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 保存验证码到 Redis 并设置过期时间
    public void saveVerificationCode(String key, String code, long expireTime, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, code, expireTime, timeUnit);
    }

    // 从 Redis 获取验证码
    public String getVerificationCode(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 删除 Redis 中的验证码
    public void deleteVerificationCode(String key) {
        stringRedisTemplate.delete(key);
    }
}