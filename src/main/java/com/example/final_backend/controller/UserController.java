package com.example.final_backend.controller;

import com.example.final_backend.dto.UserProfileDto;
import com.example.final_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam String id) {
        return userService.getProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestParam String id, @RequestBody UserProfileDto dto) {
        boolean updated = userService.updateProfile(id, dto);
        if (updated) {
            return ResponseEntity.ok("프로필이 수정되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }
    }
}
