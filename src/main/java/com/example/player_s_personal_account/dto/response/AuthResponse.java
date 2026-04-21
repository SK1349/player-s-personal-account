package com.example.player_s_personal_account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class AuthResponse {
    private String token;
    private Long userId;
    private String nickname;
    private String email;
}