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
public class Post extends TimeStamped {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id",updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String titleImageUrl;

    @Column(nullable = false)
    private String subImageUrl1;

    @Column(nullable = false)
    private String subImageUrl2;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Post(Member member, String content, String titleImageUrl, String subImageUrl1, String subImageUrl2) {
        this.nickname = member.getNickname();
        this.content = content;
        this.titleImageUrl = titleImageUrl;
        this.subImageUrl1 = subImageUrl1;
        this.subImageUrl2 = subImageUrl2;
        this.member = member; // 연관관계
    }
}
