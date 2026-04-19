package com.example.player_s_personal_account.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMatchPlayerRequest {
    @NotNull
    private Long matchId;

    @NotNull
    private Long userId;

    @NotNull
    @Size(min=1, max=10)
    private String result;

    @NotNull
    private Integer kills;

    @NotNull
    private Integer deaths;

    @NotNull
    private Integer assists;
}