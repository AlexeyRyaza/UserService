package com.app.userservice.controllers;

import com.app.userservice.services.CardService;
import com.app.userservice.services.dto.CardInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;
    
    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/create")
    ResponseEntity<CompletableFuture<CardInfoDto>> createCard(@RequestBody CardInfoDto userDto) {
        return ResponseEntity.ok().body(cardService.createCard(userDto));
    }

    @RequestMapping("/get/{id}")
    ResponseEntity<CompletableFuture<CardInfoDto>> getCard(@PathVariable int id) {
        return ResponseEntity.ok().body(cardService.findById(id));
    }

    @RequestMapping("/get")
    ResponseEntity<CompletableFuture<List<CardInfoDto>>> getCards(@RequestBody List<Integer> ids) {
        return ResponseEntity.ok().body(cardService.findCardsByIds(ids));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<CompletableFuture<CardInfoDto>> updateCard(@PathVariable int id, @RequestBody CardInfoDto userDto) {
        return ResponseEntity.ok().body(cardService.updateCard(id, userDto));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CompletableFuture<Void>> deleteCard(@PathVariable int id) {
        return ResponseEntity.ok().body(cardService.deleteCardById(id));
    }
}
