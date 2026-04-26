package com.example.player_s_personal_account.dto.response;

import com.example.player_s_personal_account.entity.MatchPlayerEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MatchHistoryResponse {
    private Long matchId;
    private LocalDateTime playedAt;
    private String mapOrMode;
    private String result;
    private Integer kills;
    private Integer deaths;
    private Double kdRatio;
    private String opponentNickname;
    private String opponentAvatarUrl;
    private String playerAvatarUrl;

    public static MatchHistoryResponse of(MatchPlayerEntity mp, String opponentNickname, Integer opponentRating,  String opponentAvatarUrl, String playerAvatarUrl) {
        double kd = mp.getDeaths() != null && mp.getDeaths() > 0
                ? Math.round((double) mp.getKills() / mp.getDeaths() * 100.0) / 100.0
                : (double) mp.getKills();

        return MatchHistoryResponse.builder()
                .matchId(mp.getMatch().getId())
                .playedAt(mp.getMatch().getPlayedAt())
                .mapOrMode(mp.getMatch().getMapOrMode())
                .result(mp.getResult())
                .kills(mp.getKills())
                .deaths(mp.getDeaths())
                .kdRatio(kd)
                .opponentNickname(opponentNickname != null ? opponentNickname : "Unknown")
                .opponentAvatarUrl(opponentAvatarUrl)
                .playerAvatarUrl(playerAvatarUrl)
                .build();
    }
}