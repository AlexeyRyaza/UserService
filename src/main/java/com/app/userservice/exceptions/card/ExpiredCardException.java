package com.app.userservice.exceptions.card;

public class ExpiredCardException extends RuntimeException {
    public ExpiredCardException(Long cardId) {
        super("Card with id " + cardId + " is expired");
    }
}
