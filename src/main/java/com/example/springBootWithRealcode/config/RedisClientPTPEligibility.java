package com.example.springBootWithRealcode.config;

import com.example.springBootWithRealcode.model.PTPEligibilityPersistanceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisClientPTPEligibility {

    public static final String KEYPREFIX = "ptpEligibility::";

    @Autowired
    RedisTemplate<String, PTPEligibilityPersistanceRecord> restTemplatePTPEligibility;

    @Value("${spring.redis.ptpEligibility.ttl.secs}")
    private int ptpEligibilityTtl;

    public void put(String redisKey,PTPEligibilityPersistanceRecord value){
        restTemplatePTPEligibility.opsForValue().set(KEYPREFIX + redisKey, value, ptpEligibilityTtl, TimeUnit.SECONDS);
    }
    public PTPEligibilityPersistanceRecord get(String redisKey){
        return restTemplatePTPEligibility.opsForValue().get(KEYPREFIX + redisKey);
    }
    public void delete(String redisKey){
         restTemplatePTPEligibility.delete(KEYPREFIX + redisKey);
    }


}
