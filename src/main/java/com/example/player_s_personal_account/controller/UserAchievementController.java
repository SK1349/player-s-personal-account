package com.example.player_s_personal_account.controller;

import com.example.player_s_personal_account.dto.response.UserAchievementResponse;
import com.example.player_s_personal_account.routes.UserAchievementRoutes;
import com.example.player_s_personal_account.service.UserAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UserAchievementRoutes.BASE)
@RequiredArgsConstructor
public class UserAchievementController {

    private final UserAchievementService service;

    @GetMapping
    public List<UserAchievementResponse> getUserAchievements(@PathVariable Long userId) {
        return service.getUserAchievements(userId);
    }

    @PostMapping(UserAchievementRoutes.GRANT_BY_CODE)
    public UserAchievementResponse grantAchievement(
            @PathVariable Long userId,
            @PathVariable String achievementCode
    ) {
        return service.grantAchievement(userId, achievementCode);
    }
}