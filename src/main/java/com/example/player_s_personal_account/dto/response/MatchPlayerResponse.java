package com.example.player_s_personal_account.dto.response;
import com.example.player_s_personal_account.entity.MatchPlayerEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MatchPlayerResponse {
    private Long id;
    private Long matchId;
    private Long userId;
    private String result;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    public static MatchPlayerResponse of(MatchPlayerEntity e) {
        return MatchPlayerResponse.builder()
                .id(e.getId())
                .matchId(e.getMatch().getId())
                .userId(e.getUser().getId())
                .result(e.getResult()).kills(e.getKills())
                .deaths(e.getDeaths()).assists(e.getAssists())
                .build();
    }
}