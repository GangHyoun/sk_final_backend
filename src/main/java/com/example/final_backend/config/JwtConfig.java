package com.example.final_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * application.properties에 정의된 JWT 설정을 바인딩
 * - JwtService에서 토큰 생성 시 필요한 필드를 해당 클래스를 통해 주입 받음
 */

@Configuration
// application.properties에 jwt.secret 등으로 시작하는 항목 자동 주입
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {
    // JWT 비밀키
    private String secret;
    // AccessToken 유효 기간 지정 (1시간)
    private long expirationMs = 3600000; // 기본값 1시간
    // RefreshToken 유효 기간 지정 (7일)
    private long refreshExpirationMs = 604800000; // 7일
}
