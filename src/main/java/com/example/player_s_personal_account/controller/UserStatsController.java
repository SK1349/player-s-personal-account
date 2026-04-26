package com.example.player_s_personal_account.controller;

import com.example.player_s_personal_account.dto.response.UserStatsResponse;
import com.example.player_s_personal_account.routes.UserStatsRoutes;
import com.example.player_s_personal_account.service.StatsCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserStatsRoutes.BASE)
@RequiredArgsConstructor
public class UserStatsController {
    private final StatsCalculationService statsCalculationService;

    @GetMapping(UserStatsRoutes.BY_USER_ID)
    public UserStatsResponse getStats(@PathVariable Long userId) {
        return statsCalculationService.calculateStats(userId);
    }
}