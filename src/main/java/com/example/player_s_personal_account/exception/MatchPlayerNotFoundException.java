package com.example.player_s_personal_account.exception;
public class MatchPlayerNotFoundException extends RuntimeException {
    public MatchPlayerNotFoundException(Long id) {
        super("MatchPlayer record with id " + id + " not found");
    }
}