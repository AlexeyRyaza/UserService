package com.app.userservice.services.cache.interfaces;

import com.app.userservice.services.dto.cardInfo.CardInfoDto;

import java.util.List;
import java.util.Optional;

public interface CardCacheService {
    Optional<CardInfoDto> getByIdFromCache(int id);
    void putInCache(CardInfoDto card);
    void deleteFromCache(int id);

    void putAllForUser(int userId, List<CardInfoDto> cards);
}
