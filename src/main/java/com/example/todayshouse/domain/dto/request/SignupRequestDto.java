package com.example.todayshouse.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class SignupRequestDto {

    // 이메일 형식
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일 형식이 아닙니다.")
    private String email;

    // 비밀번호 형식: 영문, 숫자를 포함한 8자 이상의 비밀번호를 입력
    @Pattern(regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9])[A-Za-z\\d~!@#$%^&*()+|=]{8,}$", message = "영문, 숫자를 포함한 8자 이상의 비밀번호를 입력해주세요.")
    private String password;

    // 닉네임: 2~15자
    @Size(min = 2, max = 15, message = "2자 이상 15자 이내로 입력해주세요.")
    @NotBlank(message = "2자 이상 15자 이내로 입력해주세요.")
    private String nickname;

    @NotNull(message = "필수 항목을 확인해주세요.")
    Boolean checkAge;

    @NotNull(message = "필수 항목을 확인해주세요.")
    Boolean checkTerms;

    @NotNull(message = "필수 항목을 확인해주세요.")
    Boolean checkPersonalInfo;


    @Builder.Default
    Boolean checkPersonalMarketing = false;

    @Builder.Default
    Boolean checkPushNotification = false;
}
