package com.example.player_s_personal_account.controller;
import com.example.player_s_personal_account.dto.request.CreateMatchPlayerRequest;
import com.example.player_s_personal_account.dto.request.UpdateMatchPlayerRequest;
import com.example.player_s_personal_account.dto.response.MatchPlayerResponse;
import com.example.player_s_personal_account.routes.MatchPlayerRoutes;
import com.example.player_s_personal_account.service.MatchPlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(MatchPlayerRoutes.BASE)
@RequiredArgsConstructor

public class MatchPlayerController {

    private final MatchPlayerService svc;

    @PostMapping
    public MatchPlayerResponse create(@RequestBody @Valid CreateMatchPlayerRequest r) {
        return svc.create(r);
    }

    @GetMapping
    public List<MatchPlayerResponse> getAll() {
        return svc.getAll();
    }

    @GetMapping(MatchPlayerRoutes.BY_ID)
    public MatchPlayerResponse getById(@PathVariable Long id) {
        return svc.getById(id);
    }

    @PutMapping(MatchPlayerRoutes.BY_ID)
    public MatchPlayerResponse update(@PathVariable Long id, @RequestBody @Valid UpdateMatchPlayerRequest r) {
        return svc.update(id, r);
    }

    @DeleteMapping(MatchPlayerRoutes.BY_ID)
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }
}