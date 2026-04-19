package com.example.player_s_personal_account.exception;

public class UserStatsNotFoundException extends RuntimeException {
    public UserStatsNotFoundException(Long userId) {
        super("Stats for user " + userId + " not found");
    }
}