package com.app.userservice.services;

import com.app.userservice.exceptions.card.CardNotFoundException;
import com.app.userservice.models.CardInfo;
import com.app.userservice.repos.CardRepository;
import com.app.userservice.services.dto.cardInfo.CardInfoCreateDto;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import com.app.userservice.services.mapper.CardInfoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final CardInfoMapper cardMapper;

    @Autowired
    public CardService(CardRepository cardRepository, CardInfoMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Transactional
    public CompletableFuture<CardInfoDto> createCard(CardInfoCreateDto cardCreateDto) {
        CardInfo card = cardMapper.toEntity(cardCreateDto);
        card = cardRepository.save(card);
        return CompletableFuture.completedFuture(cardMapper.toDto(card));
    }

    public CompletableFuture<CardInfoDto> findById(int id) {
        CardInfo card = cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Card with ID " + id + " not found"));

        return CompletableFuture.completedFuture(cardMapper.toDto(card));
    }

    public CompletableFuture<List<CardInfoDto>> findCardsByIds(List<Integer> ids) {
        if(ids.isEmpty()) {
            throw new IllegalArgumentException("Ids cannot be empty");
        }

        List<CardInfo> cards = cardRepository.findAllById(ids);
        List<CardInfoDto> dtos = cards.stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(dtos);
    }

    @Transactional
    public CompletableFuture<CardInfoDto> updateCard(int id, CardInfoDto updatedDto) {
        CardInfo existingCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        cardMapper.updateEntityFromDto(updatedDto, existingCard);

        return CompletableFuture.completedFuture(cardMapper.toDto(existingCard));
    }

    @Transactional
    public CompletableFuture<Void> deleteCardById(int id) {
        if(!cardRepository.existsById(id)) {
            throw new CardNotFoundException(id);
        }

        cardRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }
}
