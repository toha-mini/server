package com.example.todayshouse.controller;

import com.example.todayshouse.domain.dto.request.LikeRequestDto;
import com.example.todayshouse.domain.dto.response.LikeResponseDto;
import com.example.todayshouse.security.userdetails.UserDetailsImpl;
import com.example.todayshouse.service.LikePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikePostController {

    private final LikePostService likePostService;

    @PostMapping("/like")
    public ResponseEntity<LikeResponseDto> likePost(@Valid @RequestBody LikeRequestDto requestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likePostService.likePost(requestDto, userDetails.getMember().getId());
    }
}
