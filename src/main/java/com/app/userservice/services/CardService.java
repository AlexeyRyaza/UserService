package com.app.userservice.services;

import com.app.userservice.exceptions.card.CardNotFoundException;
import com.app.userservice.exceptions.user.UserNotFoundException;
import com.app.userservice.models.CardInfo;
import com.app.userservice.models.User;
import com.app.userservice.repos.CardRepository;
import com.app.userservice.services.dto.cardInfo.CardInfoCreateDto;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import com.app.userservice.services.mapper.CardInfoMapper;
import com.app.userservice.services.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final CardInfoMapper cardMapper;
    private final UserService userService;

    @Autowired
    public CardService(CardRepository cardRepository, CardInfoMapper cardMapper, UserService userService) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.userService = userService;
    }

    @Transactional
    public CardInfoDto createCard(CardInfoCreateDto cardCreateDto) {
        if (!userService.existsById(cardCreateDto.getUserId())) {
            throw new UserNotFoundException(cardCreateDto.getUserId());
        }

        CardInfo card = cardMapper.toEntity(cardCreateDto);
        User user = userService.findUserById(cardCreateDto.getUserId());
        card.setUser(user);

        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    public CardInfoDto findById(int id) {
        CardInfo card = cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException(id));

        return cardMapper.toDto(card);
    }

    public List<CardInfoDto> findCardsByIds(List<Integer> ids) {
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("Ids cannot be empty");
        }

        List<CardInfo> cards = cardRepository.findAllById(ids);
        return cards.stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CardInfoDto updateCard(int id, CardInfoDto updatedDto) {
        CardInfo existingCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        cardMapper.updateEntityFromDto(updatedDto, existingCard);

        return cardMapper.toDto(existingCard);
    }

    @Transactional
    public void deleteCardById(int id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException(id);
        }

        cardRepository.deleteById(id);
    }

    public boolean existsById(int id) {
        return cardRepository.existsById(id);
    }
}
