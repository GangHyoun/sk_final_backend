package com.example.final_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class PurgoClientConfig {

    @Value("${PURGO_CLIENT_API_KEY}")
    private String apiKey;

    @Value("${PURGO_PROXY_BASE_URL}")
    private String baseUrl;

    @Bean
    public RestTemplate purgoRestTemplate(RestTemplateBuilder builder) {

        // ✅ 요청 바디 디버깅용 인터셉터
        ClientHttpRequestInterceptor loggingInterceptor = (request, body, execution) -> {
            System.out.println("🧪 요청 바디 내용 확인: " + new String(body, StandardCharsets.UTF_8));
            System.out.println("🧪 Content-Type 헤더: " + request.getHeaders().getContentType());
            return execution.execute(request, body);
        };

        // ✅ Authorization 헤더 설정용 인터셉터
        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            request.getHeaders().set("Authorization", "Bearer " + apiKey);
            return execution.execute(request, body);
        };

        return builder
                .additionalMessageConverters(
                        new StringHttpMessageConverter(StandardCharsets.UTF_8)  // ✅ 핵심: JSON 문자열 직렬화 유지
                )
                .additionalInterceptors(
                        loggingInterceptor,
                        authInterceptor
                )
                .rootUri(baseUrl)
                .build();
    }
}
