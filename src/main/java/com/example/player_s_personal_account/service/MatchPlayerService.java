package com.example.player_s_personal_account.service;
import com.example.player_s_personal_account.dto.request.CreateMatchPlayerRequest;
import com.example.player_s_personal_account.dto.request.UpdateMatchPlayerRequest;
import com.example.player_s_personal_account.dto.response.MatchPlayerResponse;
import com.example.player_s_personal_account.entity.MatchPlayerEntity;
import com.example.player_s_personal_account.exception.MatchNotFoundException;
import com.example.player_s_personal_account.exception.MatchPlayerNotFoundException;
import com.example.player_s_personal_account.exception.UserNotFoundException;
import com.example.player_s_personal_account.repository.MatchPlayerRepository;
import com.example.player_s_personal_account.repository.MatchRepository;
import com.example.player_s_personal_account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchPlayerService {

    private final MatchPlayerRepository repo;
    private final MatchRepository matchRepo;
    private final UserRepository userRepo;

    @Transactional
    public MatchPlayerResponse create(CreateMatchPlayerRequest req) {

        var match = matchRepo.findById(req.getMatchId()).orElseThrow(() -> new MatchNotFoundException(req.getMatchId()));

        var user = userRepo.findById(req.getUserId()).orElseThrow(() -> new UserNotFoundException(req.getUserId()));

        var entity = MatchPlayerEntity.builder()
                .match(match)
                .user(user)
                .result(req.getResult())
                .kills(req.getKills())
                .deaths(req.getDeaths())
                .assists(req.getAssists())
                .build();

        return MatchPlayerResponse.of(repo.save(entity));
    }

    public MatchPlayerResponse getById(Long id) {
        return MatchPlayerResponse.of(repo.findById(id).orElseThrow(() -> new MatchPlayerNotFoundException(id)));
    }

    @Transactional
    public MatchPlayerResponse update(Long id, UpdateMatchPlayerRequest req) {
        var entity = repo.findById(id).orElseThrow(() -> new MatchPlayerNotFoundException(id));
        if (req.getResult() != null) entity.setResult(req.getResult());
        if (req.getKills() != null) entity.setKills(req.getKills());
        if (req.getDeaths() != null) entity.setDeaths(req.getDeaths());
        if (req.getAssists() != null) entity.setAssists(req.getAssists());
        return MatchPlayerResponse.of(repo.save(entity));
    }

    public List<MatchPlayerResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(MatchPlayerResponse::of)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}