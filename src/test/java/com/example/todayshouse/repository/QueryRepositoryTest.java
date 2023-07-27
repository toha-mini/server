package com.example.todayshouse.repository;

import com.example.todayshouse.domain.dto.request.SignupRequestDto;
import com.example.todayshouse.domain.entity.LikePost;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.Post;
import com.example.todayshouse.domain.entity.ScrapPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class QueryRepositoryTest {

    @Autowired
    QueryRepository queryRepository;

    @Autowired
    LikesRepository likesRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ScrapRepository scrapRepository;

    private Member defaultMember;
    private Post defaultPost;

    @BeforeEach
    void beforeEach() {
        for (int i = 1; i <= 5; i++) {
            SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                    .email("email@abc.com" + i)
                    .password("Password1!")
                    .nickname("nickname" + i)
                    .checkAge(true)
                    .checkTerms(true)
                    .checkPersonalInfo(true)
                    .checkPersonalMarketing(true)
                    .checkPushNotification(true)
                    .build();

            defaultMember = new Member(signupRequestDto, "encodedPassword");
            memberRepository.save(defaultMember);

            defaultPost = new Post(defaultMember, "content", "titleUrl", "sub1Url", "sub2Url");
            postRepository.save(defaultPost);
        }
    }

    @Test
    @DisplayName("좋아요 개수 조회 테스트")
    void findLikeCountTest() {
        //given
        LikePost likePost = new LikePost(defaultPost, defaultMember);
        likesRepository.save(likePost);

        //when
        Long likeCount = queryRepository.findLikeCount(defaultPost.getId());

        //then
        assertThat(likeCount).isEqualTo(1);
    }

    @Test
    @DisplayName("스크랩 개수 조회 테스트")
    void findScrapCountTest() {
        //given
        ScrapPost scrapPost = new ScrapPost(defaultPost, defaultMember);
        scrapRepository.save(scrapPost);

        //when
        Long scrapCount = queryRepository.findScrapCount(defaultPost.getId());

        //then
        assertThat(scrapCount).isEqualTo(1);
        assertThat(scrapCount).isNotEqualTo(10);
    }

    @Test
    @DisplayName("게시조회시 좋아요 내림차순으로 정렬 테스트")
    void findPostsSortedByLikeCountTest() {
        // given
        Member member1 = memberRepository.findByEmail("email@abc.com1").get();
        Member member2 = memberRepository.findByEmail("email@abc.com2").get();
        Member member3 = memberRepository.findByEmail("email@abc.com3").get();

        Post post1 = postRepository.findById(1L).get();
        Post post2 = postRepository.findById(2L).get();
        Post post3 = postRepository.findById(3L).get();

        likesRepository.save(new LikePost(post1, member1));
        likesRepository.save(new LikePost(post2, member1));
        likesRepository.save(new LikePost(post3, member1));
        likesRepository.save(new LikePost(post2, member2));
        likesRepository.save(new LikePost(post3, member2));
        likesRepository.save(new LikePost(post3, member3));

        // when
        List<Post> findPostsSortedByLikeCount = queryRepository.findPostsSortedByLikeCount();

        // then
        assertThat(findPostsSortedByLikeCount.size()).isEqualTo(5);
        assertThat(findPostsSortedByLikeCount.get(0)).isEqualTo(post3);
        assertThat(findPostsSortedByLikeCount.get(1)).isEqualTo(post2);
        assertThat(findPostsSortedByLikeCount.get(2)).isEqualTo(post1);
    }

}