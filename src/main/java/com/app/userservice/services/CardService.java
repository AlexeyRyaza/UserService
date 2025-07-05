package com.app.userservice.services;

import com.app.userservice.models.CardInfo;
import com.app.userservice.repos.CardRepository;
import com.app.userservice.services.dto.CardInfoDto;
import com.app.userservice.services.mapper.CardMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Autowired
    public CardService(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Transactional
    public CompletableFuture<CardInfoDto> createCard(CardInfoDto cardDto) {
        CardInfo card = cardMapper.toEntity(cardDto);
        card = cardRepository.save(card);
        return CompletableFuture.completedFuture(cardMapper.toDto(card));
    }

    public CompletableFuture<CardInfoDto> findById(int id) {
        CardInfo card = cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Card with ID " + id + " not found"));

        return CompletableFuture.completedFuture(cardMapper.toDto(card));
    }

    public CompletableFuture<List<CardInfoDto>> findCardsByIds(List<Integer> ids) {
        List<CardInfo> cards = cardRepository.findAllById(ids);
        List<CardInfoDto> dtos = cards.stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(dtos);
    }

    @Transactional
    public CompletableFuture<CardInfoDto> updateCard(int id, CardInfoDto updatedDto) {
        CardInfo card = cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Card with ID " + id + " not found"));

        card.setNumber(updatedDto.getNumber());
        card.setHolder(updatedDto.getHolder());
        card.setExpirationDate(updatedDto.getExpirationDate());
        card = cardRepository.save(card);
        return CompletableFuture.completedFuture(cardMapper.toDto(card));
    }

    @Transactional
    public CompletableFuture<Void> deleteCardById(int id) {
        cardRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }
}
