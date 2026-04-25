package com.example.player_s_personal_account.service;

import com.example.player_s_personal_account.dto.response.UserStatsResponse;
import com.example.player_s_personal_account.entity.MatchPlayerEntity;
import com.example.player_s_personal_account.entity.UserEntity;
import com.example.player_s_personal_account.entity.UserStatsEntity;
import com.example.player_s_personal_account.exception.UserNotFoundException;
import com.example.player_s_personal_account.repository.MatchPlayerRepository;
import com.example.player_s_personal_account.repository.UserRepository;
import com.example.player_s_personal_account.repository.UserStatsRepository;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    @Transactional
    public void initAppData() {
        recalculateAll();
    }

    @Transactional
    public void recalculateAll() {
        userRepo.findAll().forEach(user -> recalculateAndSave(user.getId()));
    }

    @Transactional
    private UserStatsResponse recalculateAndSave(Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UserStatsEntity stats = statsRepo.findByUserId(userId)
                .orElseGet(() -> {
                    UserStatsEntity newStats = new UserStatsEntity();
                    newStats.setUser(user);
                    newStats.setLastProcessedMatchId(0L);
                    return newStats;
                });

        Long lastId = stats.getLastProcessedMatchId() != null ? stats.getLastProcessedMatchId() : 0L;

        List<MatchPlayerEntity> newMatches;
        if (lastId == 0) {
            newMatches = matchPlayerRepo.findByUserIdOrderByMatchPlayedAtAsc(userId);
        } else {
            newMatches = matchPlayerRepo.findByUserIdAndMatchIdGreaterThanOrderByMatchIdAsc(userId, lastId);
        }

        if (newMatches.isEmpty()) {
            return buildResponse(stats);
        }

        int currentRating = user.getRating();

        int deltaWins = 0, deltaLosses = 0, deltaDraws = 0;
        int deltaKills = 0, deltaDeaths = 0;
        Long maxMatchId = lastId;
        int deltaXp = 0;

        for (MatchPlayerEntity mp : newMatches) {
            String result = mp.getResult().toUpperCase();
            switch (result) {
                case "WIN" -> deltaWins++;
                case "LOSS" -> deltaLosses++;
                case "DRAW" -> deltaDraws++;
            }
            deltaKills += mp.getKills() != null ? mp.getKills() : 0;
            deltaDeaths += mp.getDeaths() != null ? mp.getDeaths() : 0;

            int oppRating = mp.getOpponentRatingSnapshot() != null ? mp.getOpponentRatingSnapshot() : 1000;
            currentRating = eloRatingService.calculateNewRating(currentRating, oppRating, result);
            deltaXp += calculateXpForMatch(result, currentRating, oppRating);

            maxMatchId = mp.getMatch().getId();
        }

        stats.setWins((stats.getWins() != null ? stats.getWins() : 0) + deltaWins);
        stats.setLosses((stats.getLosses() != null ? stats.getLosses() : 0) + deltaLosses);
        stats.setDraws((stats.getDraws() != null ? stats.getDraws() : 0) + deltaDraws);
        stats.setTotalKills((stats.getTotalKills() != null ? stats.getTotalKills() : 0) + deltaKills);
        stats.setTotalDeaths((stats.getTotalDeaths() != null ? stats.getTotalDeaths() : 0) + deltaDeaths);
        stats.setMatchesPlayed(stats.getWins() + stats.getLosses() + stats.getDraws());

        stats.setLastProcessedMatchId(maxMatchId);

        user.setRating(Math.max(0, currentRating));

        int currentExp = user.getExperience() != null ? user.getExperience() : 0;
        user.setExperience(currentExp + deltaXp);

        int newLevel = calculateLevelFromXp(user.getExperience());
        user.setLevel(Math.max(user.getLevel() != null ? user.getLevel() : 1, newLevel));

        userRepo.save(user);
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

    private int calculateLevelFromXp(int experience) {
        return 1 + (int) Math.sqrt(Math.max(0, experience) / 50.0);
    }

    private int calculateXpForMatch(String result, int playerRating, int opponentRating) {
        int base = switch (result) {
            case "WIN" -> 30;
            case "DRAW" -> 15;
            case "LOSS" -> 10;
            default -> 5;
        };
        int diffBonus = Math.max(0, (opponentRating - playerRating) / 100);
        return base + diffBonus;
    }
}