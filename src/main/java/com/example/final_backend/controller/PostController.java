package com.example.final_backend.controller;

import com.example.final_backend.dto.PostDto;
import com.example.final_backend.dto.PostRequestDto;
import com.example.final_backend.entity.PostEntity;
import com.example.final_backend.entity.UserEntity;
import com.example.final_backend.Repository.AuthRepository;
import com.example.final_backend.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthRepository authRepository;

    // 현재 인증된 사용자 ID 가져오기
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    // 모든 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostEntity> posts = postService.getAllPosts();
        List<PostDto> postDtos = posts.stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    // 특정 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Integer id) {
        try {
            PostEntity post = postService.incrementViewCount(id); // 조회수 증가
            return new ResponseEntity<>(PostDto.fromEntity(post), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 나의 게시글 조회
    @GetMapping("/myPosts")
    public ResponseEntity<?> getMyPosts() {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return new ResponseEntity<>("인증이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        try {
            UserEntity user = authRepository.findById(currentUserId)
                    .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

            List<PostEntity> posts = postService.getPostsByUserId(user);
            List<PostDto> postDtos = posts.stream()
                    .map(PostDto::fromEntity)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(postDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequestDto postDto) {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return new ResponseEntity<>("인증이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        try {
            PostEntity savedPost = postService.createPost(
                    postDto.getTitle(),
                    postDto.getContent(),
                    currentUserId
            );
            return new ResponseEntity<>(PostDto.fromEntity(savedPost), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Integer id, @Valid @RequestBody PostRequestDto postDto) {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return new ResponseEntity<>("인증이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        try {
            PostEntity updatedPost = postService.updatePost(
                    id,
                    postDto.getTitle(),
                    postDto.getContent(),
                    currentUserId
            );
            return new ResponseEntity<>(PostDto.fromEntity(updatedPost), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id) {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return new ResponseEntity<>("인증이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        try {
            postService.deletePost(id, currentUserId);
            return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}