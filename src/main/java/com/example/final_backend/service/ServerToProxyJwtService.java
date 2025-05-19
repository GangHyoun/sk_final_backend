package com.example.final_backend.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ServerToProxyJwtService {

    @Value("${server-to-proxy.jwt.secret}")
    private String secretKeyString;

    @Value("${server-to-proxy.jwt.expiration}")
    private long expirationMillis;

    String body;

    private Key secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    // ① JSON 문자열 만드는 메서드
    public String createJsonBody(Map<String, String> requestBodyMap) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true); // 키 정렬
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);// 공백 제거
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);         // null 제거

        // JSON 직렬화 후, Python의 `separators=(',', ':')` 방식과 동일하게 강제 처리
        String jsonBody = mapper.writeValueAsString(requestBodyMap);

        // ✅ 콜론 앞뒤, 쉼표 앞뒤 공백 제거 (Python과 동일하게)
        jsonBody = jsonBody.replaceAll("\\s*:\\s*", ":")
                .replaceAll("\\s*,\\s*", ",");

        System.out.println("직렬화 대상 Map: " + body); // Map<String, String>
        System.out.println("JSON 문자열: " + jsonBody);
        System.out.println("SHA256 해시: " + DigestUtils.sha256Hex(jsonBody));

        return jsonBody;
    }

    // ② JSON 문자열을 받아서 JWT를 생성하는 메서드
    public String generateTokenFromJson(String jsonBody) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + expirationMillis);

        String bodyHash = DigestUtils.sha256Hex(jsonBody);

        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "purgo-skfinal");
        claims.put("hash", bodyHash);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("🔵 발급한 JWT: " + jwt);
        System.out.println("🔵 직렬화된 JSON 본문: " + jsonBody);

        return jwt;
    }
}
