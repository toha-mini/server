package com.example.todayshouse.service;

import com.example.todayshouse.domain.dto.request.LikeRequestDto;
import com.example.todayshouse.domain.dto.response.LikeResponseDto;
import com.example.todayshouse.domain.entity.LikePost;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.Post;
import com.example.todayshouse.repository.LikesRepository;
import com.example.todayshouse.repository.MemberRepository;
import com.example.todayshouse.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikePostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    // 게시글 좋아요
    public ResponseEntity<LikeResponseDto> likePost(LikeRequestDto requestDto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("사용자를 찾을수 없습니다."));
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new IllegalArgumentException("게시글을 찾을수 없습니다."));

        Optional<LikePost> likePost =likesRepository.findByPostIdAndMemberId(requestDto.getPostId(), memberId);

        //게시글이 좋아요 되어 있는 경우
        if (likePost.isPresent()) {
            likesRepository.delete(likePost.get());
            return ResponseEntity.status(201).body(new LikeResponseDto(false, 200, "OK"));
            // 게시글이 좋아요 안되어 있는 경우
        } else {
            likesRepository.save(new LikePost(post, member));
            return ResponseEntity.status(201).body(new LikeResponseDto(true, 200, "OK"));
        }
    }
}
