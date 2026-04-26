package com.example.player_s_personal_account.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }
    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}
