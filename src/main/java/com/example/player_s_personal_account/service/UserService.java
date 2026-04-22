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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

    private static final String UPLOAD_DIR = "./uploads/avatars/";

    private String saveNewAvatar(MultipartFile file) {
        try {
            Path dir = Paths.get(UPLOAD_DIR);
            if (!Files.exists(dir)) Files.createDirectories(dir);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destination = dir.resolve(fileName);

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return "/avatars/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Avatar saving error: " + e.getMessage(), e);
        }
    }

    private void deleteOldAvatar(String dbPath) {
        try {
            String fileName = dbPath.replace("/avatars/", "");
            Path filePath = Paths.get("./uploads/avatars/" + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {}
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

        if (request.getAvatarFile() != null && !request.getAvatarFile().isEmpty()) {
            if (user.getAvatarUrl() != null) {
                deleteOldAvatar(user.getAvatarUrl());
            }
            user.setAvatarUrl(saveNewAvatar(request.getAvatarFile()));
        }

        if (request.getOldPassword() != null || request.getNewPassword() != null || request.getConfirmNewPassword() != null) {
            if (request.getOldPassword() == null || request.getNewPassword() == null || request.getConfirmNewPassword() == null) {
                throw new IllegalArgumentException("To change the password, fill in: old password, new password and confirmation");
            }
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
                throw new IllegalArgumentException("Invalid old password");
            }
            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new IllegalArgumentException("The new password and the confirmation don't match");
            }
            if (request.getNewPassword().length() < 6) {
                throw new IllegalArgumentException("The new password must be at least 6 characters long");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        }


        if (request.getBirthDate() != null) user.setBirthDate(request.getBirthDate());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getCountry() != null) user.setCountry(request.getCountry());
        if (request.getCity() != null) user.setCity(request.getCity());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getBio() != null) user.setBio(request.getBio());

        return UserResponse.of(userRepository.save(user));
    }
}