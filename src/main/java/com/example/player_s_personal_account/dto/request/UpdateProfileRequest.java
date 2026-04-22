package com.example.player_s_personal_account.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateProfileRequest {

    @Size(min = 3, max = 50, message = "Nickname must be between 3 and 50 characters")
    private String nickname;

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Email(message = "Invalid email format")
    private String email;

    private MultipartFile avatarFile;

    private LocalDate birthDate;

    @Pattern(regexp = "^[MF]$", message = "Gender must be M or F")
    private String gender;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    @Size(min = 6)
    private String newPassword;
    private String oldPassword;
    private String confirmNewPassword;
}