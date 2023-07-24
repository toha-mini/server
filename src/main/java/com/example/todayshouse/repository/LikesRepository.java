package com.example.todayshouse.repository;

import com.example.todayshouse.domain.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<LikePost, Long> {
    Optional<LikePost> findByPostPostIdAndMemberMemberId(Long postId, Long memberId);
}
