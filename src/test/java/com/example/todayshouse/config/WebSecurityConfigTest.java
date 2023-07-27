package com.example.todayshouse.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class WebSecurityConfigTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("비밀번호 인코딩 테스트")
    void passwordEncoderTest() {
        //given
        String rowPassword = "rowPassword";

        //when
        String encodedPassword = passwordEncoder.encode(rowPassword);

        //then
        assertThat(encodedPassword).isNotEqualTo(rowPassword);
    }

    @Test
    @DisplayName("비밀번호 매칭 테스트")
    void passwordMatchTest() {
        //given
        String rowPassword = "rowPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(rowPassword);

        //when
        boolean result = passwordEncoder.matches(rowPassword, encodedPassword);
        boolean wrongPasswordResult = passwordEncoder.matches(wrongPassword, encodedPassword);

        //then
        assertThat(result).isTrue();
        assertThat(wrongPasswordResult).isFalse();
    }

}