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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkAndGrant(Long userId, UserStatsResponse stats) {

        if (stats.getWins() >= 1) {
            grantIfNotExists(userId, "FIRST_WIN");
        }

        if (stats.getMatchesPlayed() >= 50) {
            grantIfNotExists(userId, "VETERAN");
        }

        if (stats.getWins() >= 10 && stats.getLosses() == 0) {
            grantIfNotExists(userId, "UNDEFEATED");
        }

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        if (user.getRating() >= 2000) {
            grantIfNotExists(userId, "LEGEND");
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