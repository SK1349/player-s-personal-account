package com.example.player_s_personal_account.dto.response;

import com.example.player_s_personal_account.entity.MatchEntity;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class MatchResponse {
    private Long id;
    private String mapOrMode;
    private LocalDateTime playedAt;
    private String notes;
    public static MatchResponse of(MatchEntity e) {
        return MatchResponse.builder()
                .id(e.getId())
                .mapOrMode(e.getMapOrMode())
                .playedAt(e.getPlayedAt())
                .notes(e.getNotes())
                .build();
    }
}