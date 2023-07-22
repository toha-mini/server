package com.example.todayshouse.service;

import com.example.todayshouse.domain.dto.request.SignupRequestDto;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.SignCheckList;
import com.example.todayshouse.repository.MemberRepository;
import com.example.todayshouse.repository.SignCheckListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SignCheckListRepository signCheckListRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();
        Member member = new Member(signupRequestDto);
        SignCheckList signCheckList = new SignCheckList(signupRequestDto, member);

        memberRepository.save(member);
        signCheckListRepository.save(signCheckList);
    }

    public boolean checkValidate(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        return isDuplicatedEmail(email);
    }

    private boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEMAIL(email).isEmpty();
    }
}
