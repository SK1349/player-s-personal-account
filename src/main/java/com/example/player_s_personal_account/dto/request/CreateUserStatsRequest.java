package com.example.player_s_personal_account.dto.request;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserStatsRequest {
    @NotNull
    private Long userId;
    private Integer matchesPlayed;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private Integer totalKills;
    private Integer totalDeaths;
}