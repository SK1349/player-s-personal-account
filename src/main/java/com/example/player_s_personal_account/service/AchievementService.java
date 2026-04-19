package com.example.player_s_personal_account.service;

import com.example.player_s_personal_account.dto.request.CreateAchievementRequest;
import com.example.player_s_personal_account.dto.response.AchievementResponse;
import com.example.player_s_personal_account.entity.AchievementEntity;
import com.example.player_s_personal_account.exception.AchievementCodeExistsException;
import com.example.player_s_personal_account.exception.AchievementNotFoundException;
import com.example.player_s_personal_account.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository repository;

    @Transactional
    public AchievementResponse create(CreateAchievementRequest request) {
        if (repository.existsByCode(request.getCode())) {
            throw new AchievementCodeExistsException(request.getCode());
        }

        AchievementEntity entity = AchievementEntity.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .iconUrl(request.getIconUrl())
                .build();

        return AchievementResponse.of(repository.save(entity));
    }

    public AchievementResponse getById(Long id) {
        return AchievementResponse.of(
                repository.findById(id)
                        .orElseThrow(() -> new AchievementNotFoundException("ID: " + id))
        );
    }

    public AchievementResponse getByCode(String code) {
        return AchievementResponse.of(
                repository.findByCode(code)
                        .orElseThrow(() -> new AchievementNotFoundException("Code: " + code))
        );
    }

    public List<AchievementResponse> getAll() {
        return repository.findAll().stream()
                .map(AchievementResponse::of)
                .collect(Collectors.toList());
    }
}