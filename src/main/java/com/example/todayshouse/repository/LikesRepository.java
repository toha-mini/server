package com.example.todayshouse.repository;

import com.example.todayshouse.domain.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository <LikePost, Long> {
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

}
