package com.example.todayshouse.repository;

import com.example.todayshouse.domain.entity.ScrapPost;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScrapRepository extends JpaRepository <ScrapPost, Long> {
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

}
