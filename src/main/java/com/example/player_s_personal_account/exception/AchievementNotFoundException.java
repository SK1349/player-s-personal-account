package com.example.player_s_personal_account.exception;

public class AchievementNotFoundException extends RuntimeException {
    public AchievementNotFoundException(String identifier) {
      super("Achievement not found: " + identifier);
    }
}
