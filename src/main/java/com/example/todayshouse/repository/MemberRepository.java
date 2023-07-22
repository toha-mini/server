package com.example.todayshouse.repository;

import com.example.todayshouse.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEMAIL(String email);
}
