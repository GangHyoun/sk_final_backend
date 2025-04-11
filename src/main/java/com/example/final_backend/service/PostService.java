package com.example.final_backend.service;

import com.example.final_backend.Repository.PostRepository;
import com.example.final_backend.Repository.AuthRepository;
import com.example.final_backend.dto.PostDto;
import com.example.final_backend.dto.PostRequestDto;
import com.example.final_backend.entity.PostEntity;
import com.example.final_backend.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AuthRepository authRepository; // 유저 관련 저장소 추가

    // 게시글 전체 페이지네이션 조회
    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostDto::fromEntity);
    }

    // 게시글 생성
    @Transactional
    public PostDto createPost(Integer userId, PostRequestDto postRequestDto) {
        UserEntity user = authRepository.findById(userId)
                .orElse(null);

        PostEntity post = new PostEntity();
        post.setUserId(user);
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setCount(0);

        PostEntity savedPost = postRepository.save(post);
        return PostDto.fromEntity(savedPost);
    }

    // 게시글 수정
    @Transactional
    public PostDto updatePost(Integer postId, Integer userId, PostRequestDto postRequestDto) {
        PostEntity post = postRepository.findById(postId).orElse(null);


        if (post == null || post.getUserId() == null || !Objects.equals(post.getUserId().getUserId(), userId)) {
            throw new IllegalArgumentException("You are not authorized to update this post");
        }

        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());

        PostEntity updatedPost = postRepository.save(post);
        return PostDto.fromEntity(updatedPost);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Integer postId, Integer userId) {
        PostEntity post = postRepository.findById(postId).orElse(null);

        if (post == null || post.getUserId() == null || !Objects.equals(post.getUserId().getUserId(), userId)) {
            throw new IllegalArgumentException("You are not authorized to update this post");
        }

        postRepository.delete(post);
    }
}
