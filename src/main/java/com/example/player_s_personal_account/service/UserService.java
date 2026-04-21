package com.example.player_s_personal_account.service;

import com.example.player_s_personal_account.dto.request.RegisterRequest;
import com.example.player_s_personal_account.dto.request.UpdateProfileRequest;
import com.example.player_s_personal_account.dto.response.UserResponse;
import com.example.player_s_personal_account.entity.UserEntity;
import com.example.player_s_personal_account.exception.UserAlreadyExistsException;
import com.example.player_s_personal_account.exception.UserNotFoundException;
import com.example.player_s_personal_account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new UserAlreadyExistsException("nickname", request.getNickname());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("email", request.getEmail());
        }

        UserEntity user = UserEntity.builder()
                .nickname(request.getNickname())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .country(request.getCountry())
                .city(request.getCity())
                .phone(request.getPhone())
                .bio(request.getBio())
                .rating(1000)
                .level(1)
                .build();

        return UserResponse.of(userRepository.save(user));
    }

    public UserResponse getById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserResponse.of(user);
    }

    public UserResponse getByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email: " + email));
        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
            if (userRepository.existsByNickname(request.getNickname())) {
                throw new UserAlreadyExistsException("nickname", request.getNickname());
            }
            user.setNickname(request.getNickname());
        }

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException("email", request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        if (request.getBirthDate() != null) user.setBirthDate(request.getBirthDate());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getCountry() != null) user.setCountry(request.getCountry());
        if (request.getCity() != null) user.setCity(request.getCity());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getBio() != null) user.setBio(request.getBio());

        return UserResponse.of(userRepository.save(user));
    }
}