package com.example.final_backend.service;

import com.example.final_backend.dto.UserProfileDto;
import com.example.final_backend.entity.UserEntity;
import com.example.final_backend.Repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthRepository authRepository;

    public Optional<UserProfileDto> getProfile(String id) {
        return authRepository.findById(id).map(user -> {
            UserProfileDto dto = new UserProfileDto();
            dto.setUsername(user.getUsername());
            dto.setProfileImage(user.getProfileImage());
            return dto;
        });
    }

    public boolean updateProfile(String id, UserProfileDto dto) {
        return authRepository.findById(id).map(user -> {
            user.setUsername(dto.getUsername());
            user.setProfileImage(dto.getProfileImage());
            authRepository.save(user);
            return true;
        }).orElse(false);
    }
}
