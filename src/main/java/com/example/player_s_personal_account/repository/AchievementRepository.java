package com.example.player_s_personal_account.repository;

import com.example.player_s_personal_account.entity.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementEntity, Long> {
    Optional<AchievementEntity> findByCode(String code);
}
