package com.example.player_s_personal_account.controller;
import com.example.player_s_personal_account.dto.request.CreateUserStatsRequest;
import com.example.player_s_personal_account.dto.request.UpdateUserStatsRequest;
import com.example.player_s_personal_account.dto.response.UserStatsResponse;
import com.example.player_s_personal_account.routes.UserStatsRoutes;
import com.example.player_s_personal_account.service.StatsCalculationService;
import com.example.player_s_personal_account.service.UserStatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(UserStatsRoutes.BASE)
@RequiredArgsConstructor
public class UserStatsController {
    private final UserStatsService svc;

    private final StatsCalculationService statsCalculationService;

    @GetMapping(UserStatsRoutes.BY_USER_ID)
    public UserStatsResponse getStats(@PathVariable Long userId) {
        return statsCalculationService.calculateStats(userId);
    }

    @PostMapping
    public UserStatsResponse create(@RequestBody @Valid CreateUserStatsRequest r) {
        return svc.create(r);
    }
    /*

    @GetMapping
    public List<UserStatsResponse> getAll() {
        return svc.getAll();
    }

    @GetMapping(UserStatsRoutes.BY_USER_ID)
    public UserStatsResponse getByUserId(@PathVariable Long userId) {
        return svc.getByUserId(userId);
    }

    @PutMapping(UserStatsRoutes.BY_USER_ID)
    public UserStatsResponse update(@PathVariable Long userId, @RequestBody @Valid UpdateUserStatsRequest r) {
        return svc.update(userId, r);
    }

    @DeleteMapping(UserStatsRoutes.BY_USER_ID)
    public void delete(@PathVariable Long userId) {
        svc.delete(userId);
    }
    */
}