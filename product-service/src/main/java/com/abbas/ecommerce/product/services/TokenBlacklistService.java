package com.abbas.ecommerce.product.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class TokenBlacklistService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_BLACKLIST_PREFIX = "blacklist:";

    // Constructor ile bağımlılık enjeksiyonu
    public TokenBlacklistService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public boolean isTokenBlacklisted(String token) {
        Boolean exists = (Boolean) redisTemplate.opsForValue().get(TOKEN_BLACKLIST_PREFIX + token);
        return exists != null && exists;
    }
}

