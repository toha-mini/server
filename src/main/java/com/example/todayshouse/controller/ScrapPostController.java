package com.example.todayshouse.controller;

import com.example.todayshouse.domain.dto.request.ScrapRequestDto;
import com.example.todayshouse.domain.dto.response.ScrapResponseDto;
import com.example.todayshouse.security.userdetails.UserDetailsImpl;
import com.example.todayshouse.service.ScrapPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScrapPostController {

    private final ScrapPostService scrapPostService;

    @PostMapping("/scrap")
    public ResponseEntity<ScrapResponseDto> scrapPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      ScrapRequestDto requestDto) {
        return scrapPostService.scrapPost(userDetails.getMember().getMemberId(), requestDto.getPostId());
    }

}
