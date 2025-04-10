package com.example.final_backend.controller;

import com.example.final_backend.dto.AuthDto;
import com.example.final_backend.dto.JwtDto;
import com.example.final_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AuthDto dto) {
        authService.signup(dto);
        return ResponseEntity.ok("회원가입이 완료되었습니다");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody JwtDto.LoginRequest loginRequest) {
        try {
            JwtDto.TokenResponse response = authService.login(loginRequest);

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + response.getAccessToken())
                    .header("Refresh-Token", response.getRefreshToken())
                    .body("로그인 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("로그인 실패");
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody JwtDto.RefreshTokenRequest request) {
        try {
            JwtDto.TokenResponse response = authService.refreshToken(request.getRefreshToken());

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + response.getAccessToken())
                    .header("Refresh-Token", response.getRefreshToken())
                    .body("토큰 재발급 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("토큰 재발급 실패");
        }
    }


    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // 아이디 중복 검사
    @GetMapping("/checkId")
    public ResponseEntity<String> checkId(@RequestParam String id) {
        boolean isDuplicate = authService.isIdDuplicate(id);
        if (isDuplicate) {
            return ResponseEntity.badRequest().body("이미 사용 중인 ID 입니다.");
        }
        return ResponseEntity.ok("사용 가능한 ID 입니다.");
    }

    // 닉네임 중복 검사
    @GetMapping("/checkName")
    public ResponseEntity<String> checkName(@RequestParam String username) {
        boolean isDuplicate = authService.isNameDuplicate(username);
        if (isDuplicate) {
            return ResponseEntity.badRequest().body("이미 사용 중인 닉네임 입니다.");
        }
        return ResponseEntity.ok("사용 가능한 닉네임 입니다.");
    }

    @GetMapping("/test-auth")
    public ResponseEntity<String> testAuth() {
        return ResponseEntity.ok("인증 성공! 보호된 리소스에 접근했습니다.");
    }
}
