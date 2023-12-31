package com.example.todayshouse.controller;

import com.example.todayshouse.domain.dto.request.PostRequestDto;
import com.example.todayshouse.domain.dto.response.DetailPostResponseDto;
import com.example.todayshouse.domain.dto.response.LikePostResponseDto;
import com.example.todayshouse.domain.dto.response.MessageResponseDto;
import com.example.todayshouse.domain.dto.response.PostResponseDto;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.security.userdetails.UserDetailsImpl;
import com.example.todayshouse.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j(topic = "PostController")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<MessageResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestPart(value = "content") @Valid PostRequestDto requestDto,
                                                         @RequestPart(value = "titleImage") MultipartFile titleImgMultiPartFile,
                                                         @RequestPart(value = "subImage1", required = false) MultipartFile subImg1MultiPartFile,
                                                         @RequestPart(value = "subImage2", required = false) MultipartFile subImg2MultiPartFile
    ) {
        return postService.createPost(userDetails.getMember(), requestDto, titleImgMultiPartFile, subImg1MultiPartFile, subImg2MultiPartFile);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPostList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostList(Optional.ofNullable(userDetails));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<DetailPostResponseDto> getPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPost(postId, Optional.ofNullable(userDetails));
    }

    @GetMapping("/posts/like")
    public ResponseEntity<List<LikePostResponseDto>> getPostsByLike() {
        return postService.getPostsByLike();
    }
}
