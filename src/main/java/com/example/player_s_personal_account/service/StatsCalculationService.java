package com.example.player_s_personal_account.service;

import com.example.player_s_personal_account.dto.response.UserStatsResponse;
import com.example.player_s_personal_account.entity.MatchPlayerEntity;
import com.example.player_s_personal_account.exception.UserNotFoundException;
import com.example.player_s_personal_account.repository.MatchPlayerRepository;
import com.example.player_s_personal_account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsCalculationService {

    private final MatchPlayerRepository matchPlayerRepo;
    private final UserRepository userRepo;
    private final AchievementAutoGrantService achievementAutoGrant;

    @Transactional(readOnly = true)
    public UserStatsResponse calculateStats(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<MatchPlayerEntity> matches = matchPlayerRepo.findByUserId(userId);

        int wins = 0, losses = 0, draws = 0;
        int totalKills = 0, totalDeaths = 0;

        for (MatchPlayerEntity mp : matches) {
            String result = mp.getResult().toUpperCase();
            switch (result) {
                case "WIN" -> wins++;
                case "LOSS" -> losses++;
                case "DRAW" -> draws++;
            }
            totalKills += mp.getKills() != null ? mp.getKills() : 0;
            totalDeaths += mp.getDeaths() != null ? mp.getDeaths() : 0;
        }

        UserStatsResponse stats = UserStatsResponse.fromCalculation(
                userId,
                matches.size(),
                wins, losses, draws,
                totalKills, totalDeaths
        );

        achievementAutoGrant.checkAndGrant(userId, stats);

        return stats;
    }
}