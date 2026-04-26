package com.example.player_s_personal_account.controller;

import com.example.player_s_personal_account.dto.request.CreateAchievementRequest;
import com.example.player_s_personal_account.dto.response.AchievementResponse;
import com.example.player_s_personal_account.routes.AchievementRoutes;
import com.example.player_s_personal_account.service.AchievementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AchievementRoutes.BASE)
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService service;

    @GetMapping
    public List<AchievementResponse> getAll() {
        return service.getAll();
    }

    @GetMapping(AchievementRoutes.BY_ID)
    public AchievementResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }
}