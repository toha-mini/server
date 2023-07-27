package com.example.todayshouse.security.Jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("토큰 생성 테스트")
    void createTokenTest() {
        //given, when
        String memberEmail = "email@abc.com";
        String createdToken = jwtUtil.createToken(memberEmail);
        String extractToken = jwtUtil.extractToken(createdToken);

        //then
        assertThat(createdToken).startsWith("Bearer ");
        assertThat(jwtUtil.getEmail(extractToken)).isEqualTo(memberEmail);

    }

    @Test
    @DisplayName("토큰 유효시간 경과 테스트")
    void validateExpirationTest() throws Exception {
        //given
        JwtUtil testJwtUtil = new JwtUtil("ksjdfhvkxcjhviauerhvkjxvhaodfuhxvsdg", 100);
        String memberEmail = "email@abc.com";
        String createdToken = testJwtUtil.createToken(memberEmail);
        String extractToken = testJwtUtil.extractToken(createdToken);

        //when
        Thread.sleep(110);
        boolean result = jwtUtil.validateToken(extractToken);

        // then
        assertThat(result).isFalse();
    }

}