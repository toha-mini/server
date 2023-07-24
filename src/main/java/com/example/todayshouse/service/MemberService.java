package com.example.todayshouse.service;

import com.example.todayshouse.domain.dto.request.SignupRequestDto;
import com.example.todayshouse.domain.dto.response.EmailCheckResponseDto;
import com.example.todayshouse.domain.dto.response.MessageResponseDto;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.SignCheckList;
import com.example.todayshouse.repository.MemberRepository;
import com.example.todayshouse.repository.SignCheckListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SignCheckListRepository signCheckListRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<MessageResponseDto> signup(SignupRequestDto signupRequestDto) {
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        Member member = new Member(signupRequestDto, encodedPassword); // 인코딩된 패스워드 필요
        SignCheckList signCheckList = new SignCheckList(signupRequestDto, member);

        memberRepository.save(member);
        signCheckListRepository.save(signCheckList);

        MessageResponseDto response = new MessageResponseDto("회원가입 완료", 200, "OK");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<EmailCheckResponseDto> checkValidate(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        EmailCheckResponseDto response = new EmailCheckResponseDto(isDuplicatedEmail(email), 200, "OK");
        return ResponseEntity.status(200).body(response);
    }

    private boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email).isEmpty();
    }
}
