package com.example.player_s_personal_account.service;

import com.example.player_s_personal_account.dto.response.UserStatsResponse;
import com.example.player_s_personal_account.entity.AchievementEntity;
import com.example.player_s_personal_account.entity.UserAchievementEntity;
import com.example.player_s_personal_account.entity.UserEntity;
import com.example.player_s_personal_account.exception.AchievementNotFoundException;
import com.example.player_s_personal_account.repository.AchievementRepository;
import com.example.player_s_personal_account.repository.UserAchievementRepository;
import com.example.player_s_personal_account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AchievementAutoGrantService {

    private final UserAchievementRepository uaRepo;
    private final AchievementRepository aRepo;
    private final UserRepository userRepo;

    @Transactional
    public void checkAndGrant(Long userId, UserStatsResponse stats) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        if (stats.getMatchesPlayed() >= 1) {
            grantIfNotExists(userId, "FIRST_MATCH");
        }
        if (stats.getWins() >= 1) {
            grantIfNotExists(userId, "FIRST_WIN");
        }
        if (stats.getTotalKills() != null && stats.getTotalKills() >= 1) {
            grantIfNotExists(userId, "FIRST_KILL");
        }
        if (stats.getTotalDeaths() != null && stats.getTotalDeaths() >= 1) {
            grantIfNotExists(userId, "FIRST_DEATH");
        }
        if (user.getLevel() >= 2) {
            grantIfNotExists(userId, "LEVEL_2");
        }
        if (user.getLevel() >= 5) {
            grantIfNotExists(userId, "LEVEL_5");
        }
        if (user.getRating() >= 1500) {
            grantIfNotExists(userId, "RATING_1500");
        }
        if (user.getRating() >= 2000) {
            grantIfNotExists(userId, "RATING_2000");
        }
        if (stats.getTotalKills() != null && stats.getTotalKills() >= 5) {
            grantIfNotExists(userId, "KILLS_5");
        }
        if (stats.getTotalKills() != null && stats.getTotalKills() >= 10) {
            grantIfNotExists(userId, "KILLS_10");
        }
        if (stats.getMatchesPlayed() >= 5) {
            grantIfNotExists(userId, "MATCHES_5");
        }
        if (stats.getMatchesPlayed() >= 50) {
            grantIfNotExists(userId, "MATCHES_50");
        }
    }

    private void grantIfNotExists(Long userId, String achievementCode) {
        if (uaRepo.existsByUserIdAndAchievementCode(userId, achievementCode)) {
            return;
        }

        AchievementEntity achievement = aRepo.findByCode(achievementCode)
                .orElseThrow(() -> new AchievementNotFoundException(achievementCode));

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        UserAchievementEntity relation = UserAchievementEntity.builder()
                .user(user)
                .achievement(achievement)
                .build();

        uaRepo.save(relation);
    }
}