package com.example.player_s_personal_account.service;

import org.springframework.stereotype.Service;

@Service
public class EloRatingService {

    private static final int DEFAULT_K_FACTOR = 32;

    public int calculateNewRating(int currentRating, int opponentRating, String result) {
        double expected = calculateExpectedScore(currentRating, opponentRating);
        double actual = switch (result.toUpperCase()) {
            case "WIN" -> 1.0;
            case "DRAW" -> 0.5;
            case "LOSS" -> 0.0;
            default -> 0.0;
        };
        int kFactor = getKFactor(currentRating);
        return (int) Math.round(currentRating + kFactor * (actual - expected));
    }

    private double calculateExpectedScore(int playerRating, int opponentRating) {
        return 1.0 / (1.0 + Math.pow(10, (opponentRating - playerRating) / 400.0));
    }

    private int getKFactor(int rating) {
        if (rating < 1200) return 40;
        if (rating < 1800) return 32;
        if (rating < 2200) return 24;
        return 16;
    }
}