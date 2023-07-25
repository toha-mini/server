package com.example.todayshouse.domain.entity;

import com.example.todayshouse.util.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long commentId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Comment(String nickname, String content, Post post, Member member) {
        this.nickname = nickname;
        this.content = content;
        this.post = post;
        this.member = member;
    }
}