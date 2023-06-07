package com.example.memorip.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Service
public class RedisUtil { // Redis CRUD utility class
    private final RedisTemplate<String, String> redisTemplate;

    public RedisUtil(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /* 주어진 키에 해당하는 데이터를 Redis에서 가져옴 */
    public String getData(String key){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /* 주어진 키에 해당하는 데이터가 Redis에 존재하는지 확인 */
    public boolean existData(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /* 주어진 키에 해당하는 데이터를 Redis에 저장, 지정된 기간 후 데이터 만료되도록 설정 */
    public void setDataExpire(String key, String value, long duration){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    /* 주어진 키에 해당하는 데이터를 Redis에서 삭제 */
    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}