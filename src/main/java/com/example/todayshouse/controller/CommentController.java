package com.example.todayshouse.controller;

import com.example.todayshouse.domain.dto.request.CommentRequestDto;
import com.example.todayshouse.domain.dto.response.CommentListResponseDto;
import com.example.todayshouse.domain.dto.response.MessageResponseDto;
import com.example.todayshouse.security.userdetails.UserDetailsImpl;
import com.example.todayshouse.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<MessageResponseDto> createComment(@Valid @RequestBody CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(requestDto, userDetails.getMember());
    }

    @GetMapping("/comments/post/{postId}")
    public ResponseEntity<CommentListResponseDto> getCommentList (@PathVariable Long postId) {
        return commentService.getCommentList(postId);
    }
}
