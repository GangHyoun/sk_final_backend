package com.example.final_backend.Repository;

import com.example.final_backend.entity.PostEntity;
import com.example.final_backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    Page<PostEntity> findAll(Pageable pageable);
    List<PostEntity> findByUserId(UserEntity user); // PostEntity의 필드명 기준
    Page<PostEntity> findByTitleContaining(String title, Pageable pageable);
    Page<PostEntity> findByContentContaining(String content, Pageable pageable);
    Page<PostEntity> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}
