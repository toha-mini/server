package com.example.todayshouse.domain.entity;

import com.example.todayshouse.domain.dto.request.SignupRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SignCheckList {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long checkListId;

    @Column(nullable = false)
    private boolean checkAge;

    @Column(nullable = false)
    private boolean checkTerms;

    @Column(nullable = false)
    private boolean checkPersonalInfo;

    @Column(nullable = false)
    private boolean checkPersonalMarketing;

    @Column(nullable = false)
    private boolean checkPushNotification;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public SignCheckList(SignupRequestDto requestDto, Member member) {
        this.checkAge = requestDto.isCheckAge();
        this.checkTerms = requestDto.isCheckTerms();
        this.checkPersonalInfo = requestDto.isCheckPersonalInfo();
        this.checkPersonalMarketing = requestDto.isCheckPersonalMarketing();
        this.checkPushNotification = requestDto.isCheckPushNotification();
        this.member = member;
    }
}