package com.example.todayshouse.controller;

import com.example.todayshouse.domain.dto.request.SignupRequestDto;
import com.example.todayshouse.domain.dto.response.EmailCheckResponseDto;
import com.example.todayshouse.domain.dto.response.MessageResponseDto;
import com.example.todayshouse.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Signup")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/auth/sign-up")
    public ResponseEntity<MessageResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return memberService.signup(signupRequestDto);
    }

    @PostMapping("/auth/email")
    public ResponseEntity<EmailCheckResponseDto> checkValidate(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.checkValidate(signupRequestDto);
    }
}
