package com.example.player_s_personal_account.repository;

import com.example.player_s_personal_account.entity.UserAchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievementEntity, Long> {
    List<UserAchievementEntity> findByUserId(Long userId);
    boolean existsByUserIdAndAchievementCode(Long userId, String achievementCode);
}
