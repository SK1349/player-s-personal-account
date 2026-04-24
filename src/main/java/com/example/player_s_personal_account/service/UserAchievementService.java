package com.example.player_s_personal_account.service;

import com.example.player_s_personal_account.dto.response.UserAchievementResponse;
import com.example.player_s_personal_account.entity.AchievementEntity;
import com.example.player_s_personal_account.entity.UserAchievementEntity;
import com.example.player_s_personal_account.entity.UserEntity;
import com.example.player_s_personal_account.exception.AchievementAlreadyExistsException;
import com.example.player_s_personal_account.exception.AchievementNotFoundException;
import com.example.player_s_personal_account.exception.UserNotFoundException;
import com.example.player_s_personal_account.repository.AchievementRepository;
import com.example.player_s_personal_account.repository.UserAchievementRepository;
import com.example.player_s_personal_account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAchievementService {

    private final UserAchievementRepository achievementRelationRepo;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final StatsCalculationService statsCalculationService;

    @Transactional
    public UserAchievementResponse grantAchievement(Long userId, String code) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        AchievementEntity achievement = achievementRepository.findByCode(code)
                .orElseThrow(() -> new AchievementNotFoundException(code));

        if (achievementRelationRepo.existsByUserIdAndAchievementCode(userId, code)) {
            throw new AchievementAlreadyExistsException(code);
        }

        UserAchievementEntity relation = UserAchievementEntity.builder()
                .user(user)
                .achievement(achievement)
                .build();

        UserAchievementEntity saved = achievementRelationRepo.save(relation);
        return UserAchievementResponse.of(saved);
    }

    @Transactional
    public List<UserAchievementResponse> getUserAchievements(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        statsCalculationService.calculateStats(userId);

        return achievementRelationRepo.findByUserId(userId).stream()
                .map(UserAchievementResponse::of)
                .collect(Collectors.toList());
    }
}