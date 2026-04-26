package com.example.player_s_personal_account.repository;

import com.example.player_s_personal_account.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
