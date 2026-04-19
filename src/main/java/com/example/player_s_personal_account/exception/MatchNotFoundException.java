package com.example.player_s_personal_account.exception;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(Long id) {
        super("Match with id " + id + " not found");
    }
}