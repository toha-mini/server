package com.example.todayshouse.security.Jwt;

import com.example.todayshouse.domain.dto.request.LoginRequestDto;
import com.example.todayshouse.domain.dto.response.StatusResponseDto;
import com.example.todayshouse.security.userdetails.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final String SUCCESS_MESSAGE = "로그인 완료.";
    private final Integer SUCCESS_CODE = 200;
    private final String SUCCESS_STATE_MESSAGE = "OK";
    private final String FAIL_MESSAGE = "로그인 정보가 일치하지 않습니다.";
    private final Integer FAIL_CODE = 401;
    private final String FAIL_STATE_MESSAGE = "Unauthorized";

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("try login");

        try {
            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            log.info("error={}", e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("success login");
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String email = principal.getUsername();
        StatusResponseDto responseDto = new StatusResponseDto(SUCCESS_MESSAGE, SUCCESS_CODE, SUCCESS_STATE_MESSAGE);

        String token = jwtUtil.createToken(email);
        jwtUtil.writeResponseDtoToResponseBody(response, responseDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("failed login");

        response.setStatus(401);
        StatusResponseDto responseDto = new StatusResponseDto(FAIL_MESSAGE, FAIL_CODE, FAIL_STATE_MESSAGE);
        writeResponseDtoToResponseBody(response, responseDto);
    }

    private void writeResponseDtoToResponseBody(HttpServletResponse response, StatusResponseDto responseDto) throws IOException {
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }

}
