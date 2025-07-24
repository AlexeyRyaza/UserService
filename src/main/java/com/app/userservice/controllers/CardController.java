package com.app.userservice.controllers;

import com.app.userservice.exceptions.dto.ErrorResponse;
import com.app.userservice.services.CardService;
import com.app.userservice.services.dto.cardInfo.CardInfoCreateDto;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Create a new card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/")
    public ResponseEntity<CardInfoDto> createCard(@RequestBody @Valid CardInfoCreateDto userDto) {
        CardInfoDto createdCard = cardService.createCard(userDto);
        return ResponseEntity.ok(createdCard);
    }

    @Operation(summary = "Get card by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card found"),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CardInfoDto> getCard(@PathVariable int id) {
        CardInfoDto card = cardService.findById(id);
        return ResponseEntity.ok(card);
    }

    @Operation(summary = "Get multiple cards by their IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards found"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/")
    public ResponseEntity<List<CardInfoDto>> getCards(@RequestParam List<Integer> ids) {
        List<CardInfoDto> cards = cardService.findCardsByIds(ids);
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Update card by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card successfully updated"),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CardInfoDto> updateCard(@PathVariable int id, @RequestBody @Valid CardInfoDto userDto) {
        CardInfoDto updatedCard = cardService.updateCard(id, userDto);
        return ResponseEntity.ok(updatedCard);
    }

    @Operation(summary = "Delete card by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable int id) {
        cardService.deleteCardById(id);
        return ResponseEntity.ok().build();
    }
}

