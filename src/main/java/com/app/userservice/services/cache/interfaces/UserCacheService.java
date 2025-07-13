package com.app.userservice.services.cache.interfaces;

import com.app.userservice.models.User;

import java.util.Optional;

public interface UserCacheService {
    Optional<User> getFromCache(int id);
    Optional<User> getByEmailFromCache(String email);
    void putInCache(User user);
    void putByEmailInCache(User user);
    void deleteFromCache(int id);
}
