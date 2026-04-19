package com.example.player_s_personal_account.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMatchPlayerRequest {
    private String result;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
}