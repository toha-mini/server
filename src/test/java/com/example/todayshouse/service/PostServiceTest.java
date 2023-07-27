package com.example.todayshouse.service;

import com.example.todayshouse.domain.dto.request.PostRequestDto;
import com.example.todayshouse.domain.dto.request.SignupRequestDto;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.Post;
import com.example.todayshouse.repository.MemberRepository;
import com.example.todayshouse.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    private Member defaultMember;

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
    }

    @Test
    @DisplayName("게시물 저장 테스트")
    void createPostTest() {
        //given
        PostRequestDto postRequestDto = PostRequestDto
                .builder()
                .build();

        MultipartFile mockMultipartFile = new MockMultipartFile("test", "test.png", "image/png", new byte[1024]);

        //when
        postService.createPost(defaultMember, postRequestDto, mockMultipartFile, null, null);
        List<Post> findPosts = postRepository.findAll();

        //then
        assertThat(findPosts.size()).isEqualTo(1);
        assertThat(findPosts.get(0).getMember()).isEqualTo(defaultMember);
    }

    @Test
    @DisplayName("용량 초과 이미지 저장 실패 테스트")
    void overSizeImageCreateFailTest() {
        //given
        PostRequestDto postRequestDto = PostRequestDto
                .builder()
                .build();

        MultipartFile mockMultipartFile = new MockMultipartFile("test", "test.png", "image/png", new byte[4000000]);

        //when, then
        assertThatThrownBy(() -> postService.createPost(defaultMember, postRequestDto, mockMultipartFile, null, null))
                .isInstanceOf(MultipartException.class)
                .hasMessage("용량 초과입니다.(최대 3MB까지 가능합니다.)");
    }

}