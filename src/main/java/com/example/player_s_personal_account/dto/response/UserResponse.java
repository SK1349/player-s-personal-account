package com.example.player_s_personal_account.dto.response;
import com.example.player_s_personal_account.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String nickname;
    private String fullName;
    private String email;
    private String avatarUrl;
    private LocalDate birthDate;
    private String gender;
    private String country;
    private String city;
    private String phone;
    private String bio;
    private Integer rating;
    private Integer level;
    private LocalDateTime createdAt;

    public static UserResponse of(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .country(user.getCountry())
                .city(user.getCity())
                .phone(user.getPhone())
                .rating(user.getRating())
                .level(user.getLevel())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
