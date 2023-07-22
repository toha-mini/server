package com.example.todayshouse.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class SignupRequestDto {

    // 이메일 형식
    @Email
    @NotBlank
    private String email;

    // 비밀번호 형식: 영문, 숫자를 포함한 8자 이상의 비밀번호를 입력
    @Pattern(regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9])[A-Za-z\\d~!@#$%^&*()+|=]{8,}$")
    private String password;

    // 닉네임: 2~15자
    @Size(min = 2, max = 15)
    @NotBlank
    private String nickname;

    @NotBlank
    boolean checkAge;

    @NotBlank
    boolean checkTerms;

    @NotBlank
    boolean checkPersonalInfo;

    @Builder.Default
    boolean checkPersonalMarketing = false;

    @Builder.Default
    boolean checkPushNotification = false;
}
