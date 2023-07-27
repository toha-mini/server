package com.example.todayshouse.service;

import com.example.todayshouse.domain.dto.request.SignupRequestDto;
import com.example.todayshouse.domain.dto.response.ScrapResponseDto;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.Post;
import com.example.todayshouse.repository.MemberRepository;
import com.example.todayshouse.repository.PostRepository;
import com.example.todayshouse.repository.ScrapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class ScrapPostServiceTest {

    @Autowired
    ScrapRepository scrapRepository;

    @Autowired
    ScrapPostService scrapPostService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    private Member defaultMember;
    private Post defaultPost;

    @BeforeEach
    void beforeEach() {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email("email@abc.com")
                .password("Password1!")
                .nickname("nickname")
                .checkAge(true)
                .checkTerms(true)
                .checkPersonalInfo(true)
                .checkPersonalMarketing(true)
                .checkPushNotification(true)
                .build();

        defaultMember = new Member(signupRequestDto, "encodedPassword");
        memberRepository.save(defaultMember);

        defaultPost = new Post(defaultMember, "content", "titleUrl", "subUrl1", "subUrl2");
        postRepository.save(defaultPost);
    }

    @Test
    @DisplayName("특정 맴버의 특정 게시글 스크랩 유무 테스트(눌러서 스크랩한 경우)")
    void scrapPostTest() {
        //given, when
        ScrapResponseDto scrapResponseDto = scrapPostService.scrapPost(defaultMember.getId(), defaultPost.getId()).getBody();

        //then
        assertThat(scrapResponseDto.isScrap()).isTrue();
    }

    @Test
    @DisplayName("특정 맴버의 특정 게시글 스크랩 유무 테스트(눌러서 스크랩 취소한 경우)")
    void unScrapPostTest() {
        //given, when
        scrapPostService.scrapPost(defaultMember.getId(), defaultPost.getId());
        ScrapResponseDto scrapResponseDto = scrapPostService.scrapPost(defaultMember.getId(), defaultPost.getId()).getBody();

        //then
        assertThat(scrapResponseDto.isScrap()).isFalse();
    }


}