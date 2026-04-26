package com.example.player_s_personal_account.repository;

import com.example.player_s_personal_account.entity.MatchPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayerEntity, Long> {
    List<MatchPlayerEntity> findByUserId(Long userId);
    List<MatchPlayerEntity> findByUserIdOrderByMatchPlayedAtDesc(Long userId);
    List<MatchPlayerEntity> findByUserIdOrderByMatchPlayedAtAsc(Long userId);
    boolean existsByUserId(Long userId);
    Optional<MatchPlayerEntity> findByMatchIdAndUserIdNot(Long matchId, Long userId);
    List<MatchPlayerEntity> findByUserIdAndMatchIdGreaterThanOrderByMatchIdAsc(Long userId, Long lastMatchId);
    @Query("SELECT mp.playerRatingAfterMatch FROM MatchPlayerEntity mp WHERE mp.user.id = :userId ORDER BY mp.match.playedAt ASC")
    List<Integer> findPlayerRatingAfterMatchByUserIdOrderByMatchPlayedAtAsc(Long userId);
}