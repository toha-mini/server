package com.example.todayshouse.service;

import com.example.todayshouse.domain.dto.request.CommentRequestDto;
import com.example.todayshouse.domain.dto.response.MessageResponseDto;
import com.example.todayshouse.domain.entity.Comment;
import com.example.todayshouse.domain.entity.Post;
import com.example.todayshouse.repository.CommentRepository;
import com.example.todayshouse.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public ResponseEntity<MessageResponseDto> createComment(CommentRequestDto requestDto, String nickname) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다."));
        Comment comment = new Comment(requestDto.getContent(), nickname, post);
        commentRepository.save(comment);
        MessageResponseDto response = new MessageResponseDto("댓글 작성완료", 201, "CREATED");
        return ResponseEntity.status(201).body(response);
    }
}
