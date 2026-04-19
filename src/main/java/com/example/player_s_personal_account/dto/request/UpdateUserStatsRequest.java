package com.example.player_s_personal_account.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserStatsRequest {
    private Integer matchesPlayed;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private Integer totalKills;
    private Integer totalDeaths;
}