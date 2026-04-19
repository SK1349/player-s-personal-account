package com.example.player_s_personal_account.service;

import com.example.player_s_personal_account.dto.request.CreateUserStatsRequest;
import com.example.player_s_personal_account.dto.request.UpdateUserStatsRequest;
import com.example.player_s_personal_account.dto.response.UserStatsResponse;
import com.example.player_s_personal_account.entity.UserEntity;
import com.example.player_s_personal_account.entity.UserStatsEntity;
import com.example.player_s_personal_account.exception.UserNotFoundException;
import com.example.player_s_personal_account.exception.UserStatsNotFoundException;
import com.example.player_s_personal_account.repository.UserStatsRepository;
import com.example.player_s_personal_account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatsService {

    private final UserStatsRepository repo;
    private final UserRepository userRepo;

    @Transactional
    public UserStatsResponse create(CreateUserStatsRequest req) {

        UserEntity user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new UserNotFoundException(req.getUserId()));

        var entity = UserStatsEntity.builder()
                .user(user)
                .matchesPlayed(req.getMatchesPlayed() != null ? req.getMatchesPlayed() : 0)
                .wins(req.getWins() != null ? req.getWins() : 0)
                .losses(req.getLosses() != null ? req.getLosses() : 0)
                .draws(req.getDraws() != null ? req.getDraws() : 0)
                .totalKills(req.getTotalKills() != null ? req.getTotalKills() : 0)
                .totalDeaths(req.getTotalDeaths() != null ? req.getTotalDeaths() : 0)
                .build();

        return UserStatsResponse.of(repo.save(entity));
    }

    public UserStatsResponse getByUserId(Long userId) {
        return UserStatsResponse.of(repo.findById(userId)
                .orElseThrow(() -> new UserStatsNotFoundException(userId)));
    }

    @Transactional
    public UserStatsResponse update(Long userId, UpdateUserStatsRequest req) {
        var entity = repo.findById(userId)
                .orElseThrow(() -> new UserStatsNotFoundException(userId));

        if (req.getMatchesPlayed() != null) entity.setMatchesPlayed(req.getMatchesPlayed());
        if (req.getWins() != null) entity.setWins(req.getWins());
        if (req.getLosses() != null) entity.setLosses(req.getLosses());
        if (req.getDraws() != null) entity.setDraws(req.getDraws());
        if (req.getTotalKills() != null) entity.setTotalKills(req.getTotalKills());
        if (req.getTotalDeaths() != null) entity.setTotalDeaths(req.getTotalDeaths());

        return UserStatsResponse.of(repo.save(entity));
    }

    public List<UserStatsResponse> getAll() {
        return repo.findAll().stream().map(UserStatsResponse::of).toList();
    }

    @Transactional
    public void delete(Long userId) {
        repo.deleteById(userId);
    }
}