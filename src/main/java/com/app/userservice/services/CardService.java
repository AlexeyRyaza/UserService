package com.app.userservice.services;

import com.app.userservice.exceptions.card.CardNotFoundException;
import com.app.userservice.exceptions.user.UserNotFoundException;
import com.app.userservice.models.CardInfo;
import com.app.userservice.models.User;
import com.app.userservice.repos.CardRepository;
import com.app.userservice.services.cache.interfaces.CardCacheService;
import com.app.userservice.services.dto.cardInfo.CardInfoCreateDto;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import com.app.userservice.services.mapper.CardInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final CardInfoMapper cardMapper;
    private final UserService userService;
    private final CardCacheService cardCacheService;

    @Autowired
    public CardService(CardRepository cardRepository, CardInfoMapper cardMapper,
                       UserService userService, CardCacheService cardCacheService) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.userService = userService;
        this.cardCacheService = cardCacheService;
    }

    @Transactional
    public CardInfoDto createCard(CardInfoCreateDto cardCreateDto) {
        if (!userService.existsById(cardCreateDto.getUserId())) {
            throw new UserNotFoundException(cardCreateDto.getUserId());
        }
        User user = userService.findUserById(cardCreateDto.getUserId());

        CardInfo card = cardMapper.toEntity(cardCreateDto);
        card.setUser(user);

        card = cardRepository.save(card);
        CardInfoDto dto = cardMapper.toDto(card);

        cardCacheService.putInCache(dto);
        updateUserCardsCache(dto.getUserId());

        return cardMapper.toDto(card);
    }

    @Transactional(readOnly = true)
    public CardInfoDto findById(int id) {
        Optional<CardInfoDto> cached = cardCacheService.getByIdFromCache(id);
        if (cached.isPresent()) {
            return cached.get();
        }

        CardInfo card = cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException(id));

        CardInfoDto dto = cardMapper.toDto(card);
        cardCacheService.putInCache(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<CardInfoDto> findCardsByIds(List<Integer> ids) {
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("Ids cannot be empty");
        }

        List<CardInfoDto> cachedDtos = new ArrayList<>();
        List<Integer> missedIds = new ArrayList<>();

        for (Integer id : ids) {
            Optional<CardInfoDto> cached = cardCacheService.getByIdFromCache(id);
            if (cached.isPresent()) {
                cachedDtos.add(cached.get());
            } else {
                missedIds.add(id);
            }
        }

        if (!missedIds.isEmpty()) {
            List<CardInfo> dbCards = cardRepository.findAllById(missedIds);
            List<CardInfoDto> dbDtos = dbCards.stream()
                    .map(cardMapper::toDto)
                    .toList();

            dbDtos.forEach(cardCacheService::putInCache);

            cachedDtos.addAll(dbDtos);
        }

        return cachedDtos;
    }

    @Transactional
    public CardInfoDto updateCard(int id, CardInfoDto updatedDto) {
        CardInfo existingCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        cardMapper.updateEntityFromDto(updatedDto, existingCard);
        CardInfoDto dto = cardMapper.toDto(existingCard);

        cardCacheService.putInCache(dto);
        updateUserCardsCache(dto.getUserId());

        return dto;
    }

    @Transactional
    public void deleteCardById(int id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException(id);
        }

        cardRepository.deleteById(id);
        cardCacheService.deleteFromCache(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(int id) {
        Optional<CardInfoDto> card = cardCacheService.getByIdFromCache(id);
        if (card.isPresent()) {
            return true;
        }

        return cardRepository.existsById(id);
    }

    private void updateUserCardsCache(int userId) {
        List<CardInfo> allCards = cardRepository.findAllByUserId(userId);
        List<CardInfoDto> allDtos = allCards.stream()
                .map(cardMapper::toDto)
                .toList();
        cardCacheService.putAllForUser(userId, allDtos);
    }
}
