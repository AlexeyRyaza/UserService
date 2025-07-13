package com.app.userservice.exceptions.user;

import com.app.userservice.exceptions.general.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(int userId) {
        super("User with id " + userId + " not found");
    }
}
