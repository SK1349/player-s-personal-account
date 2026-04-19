package com.example.player_s_personal_account.dto.response;

import com.example.player_s_personal_account.entity.UserAchievementEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserAchievementResponse {

    private Long id;
    private Long achievementId;
    private String achievementCode;
    private String achievementName;
    private String achievementDescription;
    private String achievementIconUrl;
    private LocalDateTime unlockedAt;

    public static UserAchievementResponse of(UserAchievementEntity entity) {
        return UserAchievementResponse.builder()
                .id(entity.getId())
                .achievementId(entity.getAchievement().getId())
                .achievementCode(entity.getAchievement().getCode())
                .achievementName(entity.getAchievement().getName())
                .achievementDescription(entity.getAchievement().getDescription())
                .achievementIconUrl(entity.getAchievement().getIconUrl())
                .unlockedAt(entity.getUnlockedAt())
                .build();
    }
}