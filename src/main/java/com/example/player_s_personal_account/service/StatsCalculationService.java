package com.example.player_s_personal_account.service;

import com.example.player_s_personal_account.dto.response.UserStatsResponse;
import com.example.player_s_personal_account.entity.MatchPlayerEntity;
import com.example.player_s_personal_account.entity.UserEntity;
import com.example.player_s_personal_account.entity.UserStatsEntity;
import com.example.player_s_personal_account.exception.UserNotFoundException;
import com.example.player_s_personal_account.repository.MatchPlayerRepository;
import com.example.player_s_personal_account.repository.UserRepository;
import com.example.player_s_personal_account.repository.UserStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsCalculationService {

    private final MatchPlayerRepository matchPlayerRepo;
    private final UserRepository userRepo;
    private final UserStatsRepository statsRepo;
    private final AchievementAutoGrantService achievementAutoGrant;
    private final EloRatingService eloRatingService;

    @Transactional
    public UserStatsResponse calculateStats(Long userId) {
        return recalculateAndSave(userId);
    }

    @Transactional
    public void recalculateAll() {
        userRepo.findAll().forEach(user -> recalculateAndSave(user.getId()));
    }

    @Transactional
    private UserStatsResponse recalculateAndSave(Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<MatchPlayerEntity> matches = matchPlayerRepo.findByUserIdOrderByMatchPlayedAtAsc(userId);

        int wins = 0, losses = 0, draws = 0, totalKills = 0, totalDeaths = 0;
        int currentRating = user.getRating();

        for (MatchPlayerEntity mp : matches) {
            String result = mp.getResult().toUpperCase();
            switch (result) {
                case "WIN" -> wins++;
                case "LOSS" -> losses++;
                case "DRAW" -> draws++;
            }
            totalKills += mp.getKills() != null ? mp.getKills() : 0;
            totalDeaths += mp.getDeaths() != null ? mp.getDeaths() : 0;

            currentRating = eloRatingService.calculateNewRating(
                    currentRating, 1000, result);
        }

        user.setRating(Math.max(0, currentRating));
        user.setLevel(calculateLevelFromRating(currentRating));
        userRepo.save(user);

        UserStatsEntity stats = statsRepo.findByUserId(userId).orElseGet(() -> {
            UserStatsEntity newStats = new UserStatsEntity();
            newStats.setUser(user);
            newStats.setMatchesPlayed(0);
            newStats.setWins(0);
            newStats.setLosses(0);
            newStats.setDraws(0);
            newStats.setTotalKills(0);
            newStats.setTotalDeaths(0);
            return newStats;
        });

        stats.setMatchesPlayed(matches.size());
        stats.setWins(wins);
        stats.setLosses(losses);
        stats.setDraws(draws);
        stats.setTotalKills(totalKills);
        stats.setTotalDeaths(totalDeaths);
        statsRepo.save(stats);

        UserStatsResponse response = buildResponse(stats);
        achievementAutoGrant.checkAndGrant(userId, response);

        return response;
    }

    private UserStatsResponse buildResponse(UserStatsEntity e) {
        int matches = e.getMatchesPlayed() != null ? e.getMatchesPlayed() : 0;
        int wins = e.getWins() != null ? e.getWins() : 0;
        int deaths = e.getTotalDeaths() != null ? e.getTotalDeaths() : 0;
        int kills = e.getTotalKills() != null ? e.getTotalKills() : 0;

        double winRate = matches > 0
                ? Math.round((double) wins / matches * 1000.0) / 10.0
                : 0.0;
        double kdRatio = deaths > 0
                ? Math.round((double) kills / deaths * 100.0) / 100.0
                : (double) kills;

        return UserStatsResponse.builder()
                .userId(e.getUserId())
                .matchesPlayed(matches)
                .wins(wins)
                .losses(e.getLosses())
                .draws(e.getDraws())
                .totalKills(kills)
                .totalDeaths(deaths)
                .winRate(winRate)
                .kdRatio(kdRatio)
                .build();
    }

    private int calculateLevelFromRating(int rating) {
        int baseRating = 1000;
        return 1 + (int) Math.sqrt((rating - baseRating) / 25.0);
    }
}