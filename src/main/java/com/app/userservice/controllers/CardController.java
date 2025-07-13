package com.app.userservice.controllers;

import com.app.userservice.services.CardService;
import com.app.userservice.services.dto.cardInfo.CardInfoCreateDto;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/")
    public ResponseEntity<CardInfoDto> createCard(@RequestBody @Valid CardInfoCreateDto userDto) {
        CardInfoDto createdCard = cardService.createCard(userDto);
        return ResponseEntity.ok(createdCard);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardInfoDto> getCard(@PathVariable int id) {
        CardInfoDto card = cardService.findById(id);
        return ResponseEntity.ok(card);
    }

    @GetMapping("/")
    public ResponseEntity<List<CardInfoDto>> getCards(@RequestParam List<Integer> ids) {
        List<CardInfoDto> cards = cardService.findCardsByIds(ids);
        return ResponseEntity.ok(cards);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardInfoDto> updateCard(@PathVariable int id, @RequestBody @Valid CardInfoDto userDto) {
        CardInfoDto updatedCard = cardService.updateCard(id, userDto);
        return ResponseEntity.ok(updatedCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable int id) {
        cardService.deleteCardById(id);
        return ResponseEntity.ok().build();
    }
}
