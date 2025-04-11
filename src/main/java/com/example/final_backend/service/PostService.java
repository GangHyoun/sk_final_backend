package com.example.final_backend.service;

import com.example.final_backend.entity.PostEntity;
import com.example.final_backend.entity.UserEntity;
import com.example.final_backend.repository.PostRepository;
import com.example.final_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // 모든 게시글 조회
    public List<PostEntity> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    // 특정 게시글 조회
    public Optional<PostEntity> getPostById(Integer id) {
        return postRepository.findById(id);
    }

    // 특정 사용자의 게시글 조회
    public List<PostEntity> getPostsByUserId(UserEntity user) {
        return postRepository.findByUserId(user);
    }

    // 게시글 생성
    @Transactional
    public PostEntity createPost(String title, String content, String userId) throws Exception {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

        PostEntity post = new PostEntity();
        post.setUserId(user);
        post.setTitle(title);
        post.setContent(content);
        post.setCount(0); // 조회수 초기화

        return postRepository.save(post);
    }

    // 게시글 수정
    @Transactional
    public PostEntity updatePost(Integer postId, String title, String content, String currentUserId) throws Exception {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다 ID: " + postId));

        // 작성자만 수정 가능하도록 확인
        if (!post.getUserId().getId().equals(currentUserId)) {
            throw new Exception("해당 게시글을 수정할 권한이 없습니다.");
        }

        post.setTitle(title);
        post.setContent(content);

        return postRepository.save(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Integer postId, String currentUserId) throws Exception {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다 ID: " + postId));

        // 작성자만 삭제 가능하도록 확인
        if (!post.getUserId().getId().equals(currentUserId)) {
            throw new Exception("해당 게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    // 게시글 조회수 증가
    @Transactional
    public PostEntity incrementViewCount(Integer postId) throws Exception {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다 ID: " + postId));

        post.incrementCount();
        return postRepository.save(post);
    }
}