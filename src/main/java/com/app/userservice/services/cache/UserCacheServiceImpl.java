package com.app.userservice.services.cache;

import com.app.userservice.models.User;
import com.app.userservice.services.cache.interfaces.UserCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class UserCacheServiceImpl implements UserCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String USER_KEY_PREFIX = "user::";

    @Autowired
    public UserCacheServiceImpl(RedisTemplate<String, Object> redisTemplate,
                                ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<User> getFromCache(int id) {
        String key = USER_KEY_PREFIX + id;
        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) return Optional.empty();

        User user = objectMapper.convertValue(raw, User.class);
        return Optional.of(user);
    }

    @Override
    public Optional<User> getByEmailFromCache(String email) {
        String key = USER_KEY_PREFIX + email;
        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) return Optional.empty();

        User user = objectMapper.convertValue(raw, User.class);
        return Optional.of(user);
    }

    @Override
    public void putInCache(User user) {
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + user.getId(), user, Duration.ofMinutes(10));
    }

    @Override
    public void putByEmailInCache(User user) {
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + user.getEmail(), user, Duration.ofMinutes(10));
    }

    @Override
    public void deleteFromCache(int id) {
        redisTemplate.delete(USER_KEY_PREFIX + id);
    }
}
