package com.example.player_s_personal_account.controller;

import com.example.player_s_personal_account.dto.request.RegisterRequest;
import com.example.player_s_personal_account.dto.request.UpdateProfileRequest;
import com.example.player_s_personal_account.dto.response.UserResponse;
import com.example.player_s_personal_account.routes.UserRoutes;
import com.example.player_s_personal_account.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserRoutes.BASE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse register(@RequestBody @Valid RegisterRequest request) {
        return userService.register(request);
    }

    @GetMapping(UserRoutes.BY_ID)
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping(UserRoutes.BY_ID)
    public UserResponse updateProfile(
            @PathVariable Long id,
            @ModelAttribute @Valid UpdateProfileRequest request
    ) {
        return userService.updateProfile(id, request);
    }
}