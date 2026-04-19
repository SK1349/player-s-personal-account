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

    public static UserStatsResponse of(UserStatsEntity e) {
        return UserStatsResponse.builder()
                .userId(e.getUserId())
                .matchesPlayed(e.getMatchesPlayed())
                .wins(e.getWins())
                .losses(e.getLosses())
                .draws(e.getDraws())
                .totalKills(e.getTotalKills())
                .totalDeaths(e.getTotalDeaths())
                .build();
    }
}