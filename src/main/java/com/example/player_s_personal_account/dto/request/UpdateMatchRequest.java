package com.example.player_s_personal_account.dto.request;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMatchRequest {
    private String mapOrMode;
    @Size(max = 500) private String notes;
}