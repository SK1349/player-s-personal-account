package com.example.player_s_personal_account.dto.response;

import com.example.player_s_personal_account.entity.AchievementEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AchievementResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String iconUrl;

    public static AchievementResponse of(AchievementEntity entity){
        return AchievementResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .iconUrl(entity.getIconUrl())
                .build();
    }
}
