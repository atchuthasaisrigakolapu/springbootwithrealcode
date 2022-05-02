package com.example.springBootWithRealcode.config;

import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Slf4j
public class RedisCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        handleTimeOutExeception(exception);
        log.info("unable to get from cache" + cache.getName() + ":" + exception.getMessage());
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        handleTimeOutExeception(exception);
        log.info("unable to put from cache" + cache.getName() + ":" + exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        handleTimeOutExeception(exception);
        log.info("unable to evict from cache" + cache.getName() + ":" + exception.getMessage());
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        handleTimeOutExeception(exception);
        log.info("unable to clean from cache" + cache.getName() + ":" + exception.getMessage());
    }

    private void handleTimeOutExeception(RuntimeException exception) {
        if(exception instanceof RedisCommandTimeoutException)
            log.info("Redis timeout exception"+":"+exception.getMessage());
    }
}
