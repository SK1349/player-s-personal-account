package com.example.player_s_personal_account.controller;
import com.example.player_s_personal_account.dto.request.CreateMatchRequest;
import com.example.player_s_personal_account.dto.request.UpdateMatchRequest;
import com.example.player_s_personal_account.dto.response.MatchResponse;
import com.example.player_s_personal_account.routes.MatchRoutes;
import com.example.player_s_personal_account.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(MatchRoutes.BASE)
@RequiredArgsConstructor
public class MatchController {
    private final MatchService svc;
    @PostMapping
    public MatchResponse create(@RequestBody @Valid CreateMatchRequest r) {
        return svc.create(r);
    }

    @GetMapping
    public List<MatchResponse> getAll() {
        return svc.getAll();
    }

    @GetMapping(MatchRoutes.BY_ID)
    public MatchResponse getById(@PathVariable Long id) {
        return svc.getById(id);
    }
    @PutMapping(MatchRoutes.BY_ID)
    public MatchResponse update(@PathVariable Long id, @RequestBody @Valid UpdateMatchRequest r) {
        return svc.update(id, r);
    }

    @DeleteMapping(MatchRoutes.BY_ID)
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }
}