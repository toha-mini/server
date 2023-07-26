package com.example.todayshouse.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.todayshouse.domain.StatusEnum;
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

import static com.example.todayshouse.domain.StatusEnum.*;

@Service
@RequiredArgsConstructor
public class ScrapPostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ScrapRepository scrapRepository;

    // 게시글 스크랩
    public ResponseEntity<ScrapResponseDto> scrapPost(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("없는 사용자입니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("없는 포스트 입니다."));

        Optional<ScrapPost> scrapPost = scrapRepository.findByPostIdAndMemberId(postId, memberId);

        // 스크랩이 눌러져 있는 경우
        if (scrapPost.isPresent()) {
            scrapRepository.delete(scrapPost.get());
            return ResponseEntity.status(OK.getCode()).body(new ScrapResponseDto(false, OK.getCode(), OK.getMessage()));
        }

        // 스크랩이 눌러져 있지 않는 경우
        scrapRepository.save(new ScrapPost(post, member));
        return ResponseEntity.status(OK.getCode()).body(new ScrapResponseDto(true, OK.getCode(), OK.getMessage()));
    }
}
