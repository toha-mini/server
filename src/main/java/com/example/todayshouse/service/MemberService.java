package com.example.todayshouse.service;

import com.example.todayshouse.domain.StatusEnum;
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

import static com.example.todayshouse.domain.StatusEnum.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SignCheckListRepository signCheckListRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<MessageResponseDto> signup(SignupRequestDto signupRequestDto) {
        validationCheckList(signupRequestDto);

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        Member member = new Member(signupRequestDto, encodedPassword); // 인코딩된 패스워드 필요
        SignCheckList signCheckList = new SignCheckList(signupRequestDto, member);

        memberRepository.save(member);
        signCheckListRepository.save(signCheckList);

        MessageResponseDto response = new MessageResponseDto("회원가입 완료", OK.getCode(), OK.getMessage());
        return ResponseEntity.status(OK.getCode()).body(response);
    }

    private static void validationCheckList(SignupRequestDto signupRequestDto) {
        if (isValidCheckList(signupRequestDto)) {
            throw new IllegalArgumentException("필수 항목을 확인해주세요.");
        }
    }

    private static boolean isValidCheckList(SignupRequestDto signupRequestDto) {
        return signupRequestDto.getCheckAge() == false || signupRequestDto.getCheckTerms() == false || signupRequestDto.getCheckPersonalInfo() == false;
    }

    public ResponseEntity<EmailCheckResponseDto> checkValidate(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        boolean validateDuplicateEmail = isDuplicatedEmail(email);
        EmailCheckResponseDto response = new EmailCheckResponseDto(validateDuplicateEmail, OK.getCode(), OK.getMessage());
        return ResponseEntity.status(OK.getCode()).body(response);
    }

    private boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email).isEmpty();
    }
}
