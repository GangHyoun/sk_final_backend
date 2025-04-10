package com.example.final_backend.controller;

import com.example.final_backend.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test-redis")
public class RedisTestController {

    private final RedisService redisService;

    @GetMapping("/set")
    public String setTestValue(@RequestParam String key, @RequestParam String value) {
        redisService.setData(key, value, 10); // 10분 저장
        return "Saved to Redis: " + key + " = " + value;
    }

    @GetMapping("/get")
    public String getTestValue(@RequestParam String key) {
        String value = redisService.getData(key);
        return value != null ? "Value: " + value : "해당 키 없음";
    }
}
