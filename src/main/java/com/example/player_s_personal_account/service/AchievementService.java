package com.example.player_s_personal_account.service;

import com.example.player_s_personal_account.dto.response.AchievementResponse;
import com.example.player_s_personal_account.exception.AchievementNotFoundException;
import com.example.player_s_personal_account.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository repository;

    public AchievementResponse getById(Long id) {
        return AchievementResponse.of(
                repository.findById(id)
                        .orElseThrow(() -> new AchievementNotFoundException("ID: " + id))
        );
    }

    public List<AchievementResponse> getAll() {
        return repository.findAll().stream()
                .map(AchievementResponse::of)
                .collect(Collectors.toList());
    }
}