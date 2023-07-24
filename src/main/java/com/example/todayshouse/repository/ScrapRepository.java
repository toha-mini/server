package com.example.todayshouse.repository;

import com.example.todayshouse.domain.entity.ScrapPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<ScrapPost, Long> {
    Optional<ScrapPost> findByPostPostIdAndMemberMemberId(Long postId, Long memberId);
}
