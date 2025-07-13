package com.app.userservice.exceptions.card;

import com.app.userservice.exceptions.general.EntityNotFoundException;

public class CardNotFoundException extends EntityNotFoundException {
    public CardNotFoundException(int cardId) {
        super("Card with id " + cardId + " not found");
    }
}

