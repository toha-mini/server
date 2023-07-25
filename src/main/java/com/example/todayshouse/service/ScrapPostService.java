package com.example.todayshouse.service;

import com.example.todayshouse.domain.dto.response.ScrapResponseDto;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.Post;
import com.example.todayshouse.domain.entity.ScrapPost;
import com.example.todayshouse.repository.MemberRepository;
import com.example.todayshouse.repository.PostRepository;
import com.example.todayshouse.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapPostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ScrapRepository scrapRepository;

    // 게시글 스크랩
    public ResponseEntity<ScrapResponseDto> scrapPost(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 없습니다.")
        );
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다.")
        );

        Optional<ScrapPost> scrapPost = scrapRepository.findByPostIdAndMemberId(postId, memberId);

        // 스크랩이 눌러져 있는 경우
        if (scrapPost.isPresent()) {
            scrapRepository.delete(scrapPost.get());
            return ResponseEntity.status(200).body(new ScrapResponseDto(false, 200, "OK"));
            // 스크랩이 눌러져 있지 않는 경우
        } else {
            scrapRepository.save(new ScrapPost(post, member));
            return ResponseEntity.status(200).body(new ScrapResponseDto(true, 200, "OK"));
        }
    }
}
