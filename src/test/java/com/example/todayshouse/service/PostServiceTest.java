package com.example.todayshouse.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.todayshouse.domain.dto.request.PostRequestDto;
import com.example.todayshouse.domain.dto.request.SignupRequestDto;
import com.example.todayshouse.domain.dto.response.DetailPostResponseDto;
import com.example.todayshouse.domain.dto.response.LikePostResponseDto;
import com.example.todayshouse.domain.dto.response.PostResponseDto;
import com.example.todayshouse.domain.entity.LikePost;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.Post;
import com.example.todayshouse.repository.LikesRepository;
import com.example.todayshouse.repository.MemberRepository;
import com.example.todayshouse.repository.PostRepository;
import com.example.todayshouse.security.userdetails.UserDetailsImpl;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Autowired
    LikesRepository likesRepository;

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
        createPost(1024);
        List<Post> findPosts = postRepository.findAll();
        //when

        //then
        assertThat(findPosts.size()).isEqualTo(1);
        assertThat(findPosts.get(0).getMember()).isEqualTo(defaultMember);
    }

    @Test
    @DisplayName("용량 초과 이미지 저장 실패 테스트")
    void overSizeImageCreateFailTest() {
        //given, when, then
        assertThatThrownBy(() -> createPost(4000000))
                .isInstanceOf(MultipartException.class)
                .hasMessage("용량 초과입니다.(최대 3MB까지 가능합니다.)");
    }

    @Test
    @DisplayName("게시물 전체 조회 테스트")
    void getPostListTest() {
        //given
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);
        Optional<UserDetailsImpl> optionalUserDetails = Optional.of(mockUserDetails);
        createPost(100000);
        createPost(100000);
        createPost(100000);
        when(mockUserDetails.getMember()).thenReturn(defaultMember);

        //when
        List<PostResponseDto> result = postService.getPostList(optionalUserDetails).getBody();

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    private void createPost(int fileSize) {
        PostRequestDto postRequestDto = PostRequestDto
                .builder()
                .build();

        MultipartFile mockMultipartFile = new MockMultipartFile("test", "test.png", "image/png", new byte[fileSize]);

        postService.createPost(defaultMember, postRequestDto, mockMultipartFile, null, null);
    }

    @Test
    @DisplayName("빈 게시물 전체 조회 테스트")
    void gePostListTest() {
        //given
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);
        Optional<UserDetailsImpl> optionalUserDetails = Optional.of(mockUserDetails);

        //when
        List<PostResponseDto> result = postService.getPostList(optionalUserDetails).getBody();

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("게시물 단건 조회 테스트")
    void getPostTest() {
        //given
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);
        Optional<UserDetailsImpl> optionalUserDetails = Optional.of(mockUserDetails);
        createPost(10000);
        when(mockUserDetails.getMember()).thenReturn(defaultMember);

        Post findPost = postRepository.findAll().get(0);
        Long postId = findPost.getId();

        //when
        DetailPostResponseDto result = postService.getPost(postId, optionalUserDetails).getBody();

        //then
        assertThat(result.getContent()).isEqualTo(findPost.getContent());
        assertThat(result.getCreatedAt()).isEqualTo(findPost.getCreatedAt());
    }

    @Test
    @DisplayName("게시물 단건 조회 실패 테스트(잘못된 postId로 조회 시도)")
    void wrongIdGetPostTest() {
        //given
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);
        Optional<UserDetailsImpl> optionalUserDetails = Optional.of(mockUserDetails);
        createPost(10000);
        when(mockUserDetails.getMember()).thenReturn(defaultMember);

        Post findPost = postRepository.findAll().get(0);
        Long wrongNumber = 1123124124515L;

        // when, then
        assertThatThrownBy(() -> postService.getPost(wrongNumber, optionalUserDetails))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("없는 포스트입니다.");
    }

    @Test
    @DisplayName("좋아요 순으로 전체 게시물 조회")
    void getPostsByLikeTest() {
        //given
        createPost(10000);
        createPost(10000);
        createPost(10000);

        List<Post> findPostList = postRepository.findAll();

        Post findFirstPost = findPostList.get(0);
        Post findSecondPost = findPostList.get(1);
        Post findThirdPost = findPostList.get(2);

        LikePost likePost = new LikePost(findThirdPost, defaultMember);
        likesRepository.save(likePost);

        //when
        List<LikePostResponseDto> postListByLike = postService.getPostsByLike().getBody();
        LikePostResponseDto firstDto = postListByLike.get(0);

        //then
        assertThat(firstDto.getPostId()).isEqualTo(findThirdPost.getId());
    }




}