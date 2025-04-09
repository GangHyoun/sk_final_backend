package com.example.final_backend.service;

import com.example.final_backend.Repository.UserRepository;
import com.example.final_backend.dto.UserDto;
import com.example.final_backend.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 1. email 중복 확인 따로 빼기
 * 2. 비번 암호화 저장
 * 3. 이메일 확인 시 회원가입되도록
 * 4. 회원가입 시 프로필 이미지 필수 x
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public void signup(UserDto dto) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 사용자 엔티티 생성 및 저장
        UserEntity user = new UserEntity();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPw(dto.getPw()); // 추후에 암호화 필요
        user.setProfileImage(dto.getProfileImage());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        // 이메일 전송
        sendWelcomeEmail(user.getEmail(), user.getUsername());
    }

    private void sendWelcomeEmail(String email, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("회원가입을 환영합니다!");
        message.setText(username + "님, 환영합니다. 가입을 축하드립니다!");
        mailSender.send(message);
    }
}
