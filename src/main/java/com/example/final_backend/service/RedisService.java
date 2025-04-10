package com.example.final_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setData(String key, String value, long timeoutInMinutes) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(timeoutInMinutes));
    }

    public String getData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
}
