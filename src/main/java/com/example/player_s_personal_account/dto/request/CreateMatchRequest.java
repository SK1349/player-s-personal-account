package com.example.player_s_personal_account.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMatchRequest {
    @NotBlank
    private String mapOrMode;
    @Size(max = 500)
    private String notes;
}