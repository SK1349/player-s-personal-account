package com.example.player_s_personal_account.controller;

import com.example.player_s_personal_account.dto.request.LoginRequest;
import com.example.player_s_personal_account.dto.request.RegisterRequest;
import com.example.player_s_personal_account.dto.response.AuthResponse;
import com.example.player_s_personal_account.dto.response.UserResponse;
import com.example.player_s_personal_account.security.JwtUtil;
import com.example.player_s_personal_account.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getEmail());
        var user = userService.getByEmail(request.getEmail());

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build());
    }
}