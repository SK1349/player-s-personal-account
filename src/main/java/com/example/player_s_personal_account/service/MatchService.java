package com.example.player_s_personal_account.service;
import com.example.player_s_personal_account.dto.request.CreateMatchRequest;
import com.example.player_s_personal_account.dto.request.UpdateMatchRequest;
import com.example.player_s_personal_account.dto.response.MatchResponse;
import com.example.player_s_personal_account.entity.MatchEntity;
import com.example.player_s_personal_account.exception.MatchNotFoundException;
import com.example.player_s_personal_account.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository repo;

    @Transactional
    public MatchResponse create(CreateMatchRequest req) {
        return MatchResponse.of(repo.save(MatchEntity.builder()
                .mapOrMode(req.getMapOrMode())
                .notes(req.getNotes())
                .build()));
    }

    public MatchResponse getById(Long id) {
        return MatchResponse.of(repo.findById(id).orElseThrow(() -> new MatchNotFoundException(id)));
    }

    @Transactional
    public MatchResponse update(Long id, UpdateMatchRequest req) {
        MatchEntity m = repo.findById(id).orElseThrow(() -> new MatchNotFoundException(id));
        if (req.getMapOrMode() != null)
            m.setMapOrMode(req.getMapOrMode());
        if (req.getNotes() != null)
            m.setNotes(req.getNotes());
        return MatchResponse.of(repo.save(m));
    }

    public List<MatchResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(MatchResponse::of)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}