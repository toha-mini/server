package com.example.todayshouse.service;

import com.amazonaws.services.kms.model.NotFoundException;
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

import static com.example.todayshouse.domain.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ResponseEntity<MessageResponseDto> createComment(CommentRequestDto requestDto, Member member) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new NotFoundException("없는 포스트입니다."));
        Comment comment = new Comment(requestDto.getContent(), member.getNickname(), post, member);

        commentRepository.save(comment);

        MessageResponseDto response = new MessageResponseDto("댓글 작성완료", CREATED.getCode(), CREATED.getMessage());

        return ResponseEntity.status(CREATED.getCode()).body(response);
    }

    public ResponseEntity<CommentListResponseDto> getCommentList(Long postId) {
        List<CommentResponseDto> commentList = commentRepository.findAllByPostId(postId).stream()
                .map(CommentResponseDto::new)
                .toList();

        CommentListResponseDto response = new CommentListResponseDto(commentList, commentList.size());

        return ResponseEntity.status(OK.getCode()).body(response);
    }
}
