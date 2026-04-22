package com.example.player_s_personal_account.dto.response;
import com.example.player_s_personal_account.entity.UserStatsEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserStatsResponse {

    private Long userId;
    private Integer matchesPlayed;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private Integer totalKills;
    private Integer totalDeaths;

    private Double winRate;
    private Double kdRatio;

    public static UserStatsResponse of(UserStatsEntity e) {
        int matches = e.getMatchesPlayed() != null ? e.getMatchesPlayed() : 0;
        int wins = e.getWins() != null ? e.getWins() : 0;
        int losses = e.getLosses() != null ? e.getLosses() : 0;
        int draws = e.getDraws() != null ? e.getDraws() : 0;
        int kills = e.getTotalKills() != null ? e.getTotalKills() : 0;
        int deaths = e.getTotalDeaths() != null ? e.getTotalDeaths() : 0;

        double winRate = matches > 0 ? Math.round((double) wins / matches * 1000.0) / 10.0 : 0.0;
        double kdRatio = deaths > 0 ? Math.round((double) kills / deaths * 100.0) / 100.0 : (double) kills;


        return UserStatsResponse.builder()
                .userId(e.getUserId())
                .matchesPlayed(matches)
                .wins(wins)
                .losses(losses)
                .draws(draws)
                .totalKills(kills)
                .totalDeaths(deaths)
                .winRate(winRate)
                .kdRatio(kdRatio)
                .build();
    }

    public static UserStatsResponse fromCalculation(Long userId, int matches, int wins, int losses, int draws, int kills, int deaths) {
        double winRate = matches > 0 ? Math.round((double) wins / matches * 1000.0) / 10.0 : 0.0;
        double kdRatio = deaths > 0 ? Math.round((double) kills / deaths * 100.0) / 100.0 : (double) kills;

        return UserStatsResponse.builder()
                .userId(userId)
                .matchesPlayed(matches)
                .wins(wins)
                .losses(losses)
                .draws(draws)
                .totalKills(kills)
                .totalDeaths(deaths)
                .winRate(winRate)
                .kdRatio(kdRatio)
                .build();
    }
}