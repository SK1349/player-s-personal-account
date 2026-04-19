package com.example.player_s_personal_account.exception;

public class AchievementAlreadyExistsException extends RuntimeException {
    public AchievementAlreadyExistsException(String code) {
        super("Achievement with code '" + code + "' already unlocked by this user");
    }
}