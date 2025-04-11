package com.example.final_backend.dto;
import com.example.final_backend.entity.PostEntity;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public class PostDto {
    private Integer postId;
    private Integer userId;
    private String username; // Added for display purposes
    private String title;
    private String content;
    private Integer count;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostDto fromEntity(PostEntity post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .userId(post.getUserId().getUserId())
                .username(post.getUserId().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .count(post.getCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
