package com.example.springBootWithRealcode.config;

import com.example.springBootWithRealcode.model.PTPEligibilityPersistanceRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport implements CachingConfigurer {


    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.ssl}")
    private String ssl;
    @Value("${spring.redis.timeout.secs}")
    private int redisTimeOutInSecs;
    @Value("${spring.redis.socket.timeout.secs}")
    private int  redisSocketTimeOutInSecs;
    @Value("${spring.redis.ttl.hours}")
    private int redisDataTTL;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        var socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofSeconds(redisSocketTimeOutInSecs))
                .build();
        var clientOptions = ClientOptions.builder()
                .socketOptions(socketOptions)
                .build();
        var clientConfiguration  = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(redisTimeOutInSecs))
                .clientOptions(clientOptions)
                .build();
        if(Boolean.parseBoolean(ssl))
            clientConfiguration =  LettuceClientConfiguration.builder()
                    .useSsl()
                    .build();

        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration();
        serverConfig.setHostName(host);
        serverConfig.setPort(Integer.parseInt(port));
        serverConfig.setPassword(RedisPassword.of(password));

        final var lettuceConnectionFactory = new LettuceConnectionFactory(serverConfig,clientConfiguration);
        lettuceConnectionFactory.setValidateConnection(true);
        return lettuceConnectionFactory;
    }

    public <T> Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer(ObjectMapper objectMapper, Class<T> classOfT){
        var copy = objectMapper.copy();
        var jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(classOfT);
        jackson2JsonRedisSerializer.setObjectMapper(copy);
        return jackson2JsonRedisSerializer;
    }
    public <T> RedisTemplate<String,T> getRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory, Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer){
        RedisTemplate<String, T> stringTRedisTemplate = new RedisTemplate<>();
        stringTRedisTemplate.setKeySerializer(RedisSerializer.string());
        stringTRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        stringTRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
        stringTRedisTemplate.afterPropertiesSet();
        return stringTRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, PTPEligibilityPersistanceRecord> redisTemplatePtpEligibility(LettuceConnectionFactory lettuceConnectionFactory, ObjectMapper objectMapper){
        return getRedisTemplate(lettuceConnectionFactory,jackson2JsonRedisSerializer(objectMapper,PTPEligibilityPersistanceRecord.class));
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory, ObjectMapper objectMapper){
        return getRedisTemplate(lettuceConnectionFactory,jackson2JsonRedisSerializer(objectMapper,Object.class));
    }
    @Bean
    public RedisCacheManager  redisCacheManager(LettuceConnectionFactory redisConnetionFactory, ObjectMapper objectMapper){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
                .entryTtl(Duration.ofHours(redisDataTTL))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        redisCacheConfiguration.usePrefix();

        RedisCacheManager redisCacheManger = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnetionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
        return redisCacheManger;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }
}
