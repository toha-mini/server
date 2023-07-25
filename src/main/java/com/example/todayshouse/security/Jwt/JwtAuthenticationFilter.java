package com.example.todayshouse.security.Jwt;

import com.example.todayshouse.domain.dto.request.LoginRequestDto;
import com.example.todayshouse.domain.dto.response.MessageResponseDto;
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

import static org.springframework.http.MediaType.*;

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
        try {
            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String email = principal.getUsername();
        MessageResponseDto responseDto = new MessageResponseDto(SUCCESS_MESSAGE, SUCCESS_CODE, SUCCESS_STATE_MESSAGE);

        String token = jwtUtil.createToken(email);

        setResponseType(response);
        jwtUtil.addTokenToHeader(token, response);
        writeResponseDtoToResponseBody(response, responseDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(FAIL_CODE);
        setResponseType(response);
        MessageResponseDto responseDto = new MessageResponseDto(FAIL_MESSAGE, FAIL_CODE, FAIL_STATE_MESSAGE);
        writeResponseDtoToResponseBody(response, responseDto);
    }

    private static void setResponseType(HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON_VALUE);
    }

    private void writeResponseDtoToResponseBody(HttpServletResponse response, MessageResponseDto responseDto) throws IOException {
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }

}
