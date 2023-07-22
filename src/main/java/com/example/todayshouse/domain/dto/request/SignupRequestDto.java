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

//    @NotBlank
//    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
//    private String email;
//
//    @NotBlank
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,15}$")          //하나 이상의 알파벳과 특수문자, 8~15자리
//    private String password;
//
//    @NotBlank
//    @Pattern(regexp = "^[a-zA-Z0-9]{5,12}$")            //특수문자 x, 5~12자리
//    private String nickname;


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
