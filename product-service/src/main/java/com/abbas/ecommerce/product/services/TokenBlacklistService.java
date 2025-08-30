package com.abbas.ecommerce.product.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_BLACKLIST_PREFIX = "blacklist:";

    public void blacklistToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, true, Duration.ofMillis(expirationMillis));
    }

    public boolean isTokenBlacklisted(String token) {
        Boolean exists = (Boolean) redisTemplate.opsForValue().get(TOKEN_BLACKLIST_PREFIX + token);
        return exists != null && exists;
    }
}

