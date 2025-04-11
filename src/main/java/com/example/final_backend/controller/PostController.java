package com.example.final_backend.controller;

import com.example.final_backend.dto.PostDto;
import com.example.final_backend.dto.PostRequestDto;
import com.example.final_backend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    @GetMapping("/list")
    public ResponseEntity<Page<PostDto>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

//    @PostMapping("/create")
//    public ResponseEntity<?> createPost(@AuthenticationPrincipal PostRequestDto postRequestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Integer userId = ((CustomUserDetails) userDetails).getId();
//
//        return new ResponseEntity<>(postService.createPost(userId, postRequestDto), HttpStatus.CREATED);
//    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestHeader("X-User-ID") Integer userId,
            @Valid @RequestBody PostRequestDto postRequestDto) {
        return new ResponseEntity<>(postService.createPost(userId, postRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable("id") Integer postId,
            @RequestHeader("X-User-ID") Integer userId,
            @Valid @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.ok(postService.updatePost(postId, userId, postRequestDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("id") Integer postId,
            @RequestHeader("X-User-ID") Integer userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
}
