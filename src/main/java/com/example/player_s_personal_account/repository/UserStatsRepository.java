package com.example.player_s_personal_account.repository;

import com.example.player_s_personal_account.entity.UserStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatsRepository extends JpaRepository<UserStatsEntity, Long> {

    Optional<UserStatsEntity> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}