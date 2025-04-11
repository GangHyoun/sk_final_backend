package com.example.final_backend.Repository;

import com.example.final_backend.entity.PostEntity;
import com.example.final_backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    List<PostEntity> findByUserId(UserEntity userId);
    List<PostEntity> findAllByOrderByCreatedAtDesc();
}
