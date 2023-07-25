package com.example.todayshouse.service;

import com.example.todayshouse.domain.dto.request.CommentRequestDto;
import com.example.todayshouse.domain.dto.response.CommentListResponseDto;
import com.example.todayshouse.domain.dto.response.CommentResponseDto;
import com.example.todayshouse.domain.dto.response.MessageResponseDto;
import com.example.todayshouse.domain.entity.Comment;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.Post;
import com.example.todayshouse.repository.CommentRepository;
import com.example.todayshouse.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public ResponseEntity<MessageResponseDto> createComment(CommentRequestDto requestDto, Member member) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다."));

        Comment comment = new Comment(requestDto.getContent(), member.getNickname(), post, member); // 연관관계 설정 추가

        commentRepository.save(comment);

        MessageResponseDto response = new MessageResponseDto("댓글 작성완료", 201, "CREATED");

        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<CommentListResponseDto> getCommentList(Long postId) {

        List<CommentResponseDto> commentList;

        commentList = commentRepository.findAllByPostId(postId).stream().map(CommentResponseDto::new).toList();

        CommentListResponseDto response = new CommentListResponseDto(commentList, commentList.size());

        return ResponseEntity.status(200).body(response);
    }
}
