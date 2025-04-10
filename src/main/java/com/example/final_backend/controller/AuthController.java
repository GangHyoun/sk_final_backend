package com.example.final_backend.controller;

import com.example.final_backend.dto.LoginRequestDto;
import com.example.final_backend.dto.AuthDto;
import com.example.final_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AuthDto dto) {
        authService.signup(dto);
        return ResponseEntity.ok("회원가입이 완료되었습니다");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        String token = authService.login(dto);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body("로그인 성공");
    }

    @GetMapping("/check-id")
    public ResponseEntity<String> checkId(@RequestParam String id) {
        boolean isDuplicate = authService.isIdDuplicate(id);
        if (isDuplicate) {
            return ResponseEntity.badRequest().body("이미 사용 중인 ID입니다.");
        }
        return ResponseEntity.ok("사용 가능한 ID입니다.");
    }

    // 로그아웃

}
