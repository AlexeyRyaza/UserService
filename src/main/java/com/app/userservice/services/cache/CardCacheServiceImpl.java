package com.app.userservice.services.cache;

import com.app.userservice.services.cache.interfaces.CardCacheService;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardCacheServiceImpl implements CardCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CARD_KEY_PREFIX = "card:";
    private static final String USER_CARDS_KEY_PREFIX = "user_cards:";

    @Override
    public Optional<CardInfoDto> getByIdFromCache(int id) {
        Object raw = redisTemplate.opsForValue().get(CARD_KEY_PREFIX + id);
        if (raw == null) {
            return Optional.empty();
        }

        CardInfoDto card = objectMapper.convertValue(raw, CardInfoDto.class);
        return Optional.of(card);
    }

    @Override
    public void putInCache(CardInfoDto card) {
        redisTemplate.opsForValue().set(CARD_KEY_PREFIX + card.getId(), card);
    }

    @Override
    public void deleteFromCache(int id) {
        redisTemplate.delete(CARD_KEY_PREFIX + id);
    }

    @Override
    public void putAllForUser(int userId, List<CardInfoDto> cards) {
        redisTemplate.opsForValue().set(USER_CARDS_KEY_PREFIX + userId, cards);
    }

    public Optional<List<CardInfoDto>> getAllForUser(int userId) {
        Object raw = redisTemplate.opsForValue().get(USER_CARDS_KEY_PREFIX + userId);
        if (raw == null) return Optional.empty();

        List<CardInfoDto> list = objectMapper.convertValue(
                raw,
                objectMapper.getTypeFactory().constructCollectionType(List.class, CardInfoDto.class)
        );
        return Optional.of(list);
    }
}
