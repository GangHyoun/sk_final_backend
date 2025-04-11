package com.example.final_backend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    @NotBlank(message = "제목을 써주세요")
    @Size(max = 255, message = "제목은 255자까지 가능합니다.")
    private String title;

    @Size(max = 1000, message = "게시글 내용은 1000자까지 가능합니다")
    private String content;
}
